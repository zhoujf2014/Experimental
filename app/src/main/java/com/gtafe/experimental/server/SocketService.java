package com.gtafe.experimental.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.gtafe.experimental.Constant.Constant;
import com.gtafe.experimental.activity.AccessControl;
import com.gtafe.experimental.app.ExperimentalApplication;
import com.gtafe.experimental.bean.DataBean;
import com.gtafe.experimental.bean.PostObj;
import com.gtafe.experimental.utils.Util;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;

/**
 * Created by ZhouJF on 2017/9/15.
 */

public class SocketService extends Service {
    private static final String TAG = "SocketService";

    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private List<OutputStream> allOut = new ArrayList<>();
    public DataBean mDataBean;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        EventBus.getDefault().register(this);
        mDataBean = ExperimentalApplication.getDataBean();
        SerialPortThread.start();
        SocketServerThread.start();
    }

    private synchronized void addOut(OutputStream out) {
        allOut.add(out);
    }

    private synchronized void removeOut(OutputStream out) {
        allOut.remove(out);
    }


    public SerialPort mSerialPort;
    private Thread SerialPortThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                mSerialPort = ExperimentalApplication.getSerialPort();
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();

                while (mInputStream != null) {
                    byte[] sbuffer = new byte[1024];
                    if (mInputStream.available() > 0) {
                        int size = mInputStream.read(sbuffer);
                        if (size < 9) {
                            Thread.sleep(30);
                            continue;
                        }
                        interpreting(sbuffer, size);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    private void interpreting(byte[] sbuffer, int size) {
        Log.e(TAG, "interpretingData:all=" + Util.byteToHexString(sbuffer));

        int temp = 0;
        while (temp < size) {
            int b = sbuffer[4 + temp];
            int length = 7 + b;
            byte[] bytes = new byte[length];
            for (int i = temp; i < temp + length; i++) {
                bytes[i - temp] = sbuffer[i];
            }
            temp += length;
            if (bytes.length >= 8 && bytes[bytes.length - 1] == 0x0A && bytes[0] == 0x7F) {
                interpretingData(bytes);
            }
        }
    }

    private boolean serving = true;

    private ServerSocket server;

    private Thread SocketServerThread = new Thread(new Runnable() {


        private boolean connectting = true;

        @Override
        public void run() {
            while (serving) {
                while (connectting) {
                    try {
                        Log.e(TAG, "run: SocketServerThread  创建ServerSocket");
                        server = new ServerSocket(8085);
                        server.setSoTimeout(10000);
                        connectting = false;
                    } catch (IOException e) {
                        Log.e(TAG, "run: SocketServerThread  创建ServerSocket失败 2秒后重新连接");
                        SystemClock.sleep(2000);
                        e.printStackTrace();
                    }
                }
                while (!server.isClosed()) {
                    try {
                        Log.e(TAG, "start: 等待客户端连接……");
                        Socket socket = server.accept();
                        Log.e(TAG, "start: 一个客户端连接了！");
                        /**
                         * 当一个客户端连接后，启动一个线程，来负责与该客户端交互
                         */
                        ClientHandler handler = new ClientHandler(socket);
                        new Thread(handler).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    private class ClientHandler implements Runnable {
        private Socket socket;
        //客户端地址信息
        private String host;
        private OutputStream mOut;
        private InputStream mIn;


        public ClientHandler(Socket socket) {
            this.socket = socket;
            //通过socket可以得知远端计算机信息
            InetAddress address = socket.getInetAddress();
            host = address.getHostAddress();
            Log.e(TAG, "ClientHandler: " + address + ":" + host);
        }

        public void run() {
            try {
                mOut = socket.getOutputStream();
                mIn = socket.getInputStream();
            } catch (Exception e) {
                Log.e(TAG, "获取输入流失败");
                e.printStackTrace();
                return;
            }
            addOut(mOut);
            try {
                while (mIn != null) {

                    byte[] bytes = new byte[1024];
                    int read = mIn.read(bytes);
                    if (read < 8) {
                        Thread.sleep(30);
                        continue;
                    }
                    interpreting(bytes, read);
                }
            } catch (Exception e) {
                Log.e(TAG, "接收wifi设备数据失败");
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        if (mOut != null) {

                            removeOut(mOut);
                            mOut.close();
                        }
                        if (mIn != null) {
                            mIn.close();
                            mIn = null;
                        }

                        if (socket.isInputShutdown()) {
                            socket.shutdownInput();

                        }
                        if (socket.isOutputShutdown()) {
                            socket.shutdownOutput();
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


//     * 0x11: 火焰传感器模块
//     0x12：人体红外探测模块
//     0x13：烟雾探测模块
//     0x14：燃气探测模块
//
//     0x21: 模拟量输入模块
//     0x22：温湿度探测模块
//     0x23：光照探测模块
//     0x24：语音模块
//     0x25: RFID读卡器
//
//     0x31: 窗帘控制模块
//     0x32: PWM控制模块
//     0x33: 电源控制模块
//     0x34: 红外转发模块
//
//     0xFF: 数据确认


    private void interpretingData(byte[] buffer) {
        Log.e(TAG, "interpretingData:all=" + Util.byteToHexString(buffer));

        byte b = 1;
        int length = buffer.length;
        switch (buffer[3]) {
            //  Double aMoisture = new Double(buffer[8] > 9 ? buffer[7] + "." + buffer[8] : buffer[7] + ".0" + buffer[8]);

            case 0x11:
                //     * 0x11: 火焰传感器模块
                if (mDataBean.setSafe_flame(buffer[5])) {
                    return;
                }


                if (buffer[5] == 0x02) {
                    //开启火焰报警
                    b = 1;
                }
                if (buffer[5] == 0x01) {
                    b = (byte) 0xff;

                }
                sendData(b);


                break;
            case 0x12:
                //     0x12：人体红外探测模块
                if (mDataBean.setSafe_infrared(buffer[5])) {
                    return;
                }

                if (buffer[5] == 0x02) {
                    if (mDataBean.isSafeState()) {
                        b = 2;
                    }
                }
                if (buffer[5] == 0x01) {

                    b = (byte) 0xff;
                }
                sendData(b);

                break;
            case 0x13:
                //     0x13：烟雾探测模块
                if (mDataBean.setSafe_smoke(buffer[5])) {
                    return;
                }
                if (buffer[5] == 0x02) {
                    //开启火焰报警
                    b = 3;
                }
                if (buffer[5] == 0x01) {
                    b = (byte) 0xff;

                }
                sendData(b);
                break;
            case 0x14:
                //     0x14：燃气探测模块
                if (mDataBean.setSafe_gas(buffer[5])) {
                    return;
                }
                if (buffer[5] == 0x02) {
                    //开启火焰报警
                    b = 4;
                }
                if (buffer[5] == 0x01) {
                    b = (byte) 0xff;

                }
                sendData(b);

                break;
            case 0x21:
                //     0x21: 模拟量输入模块

                break;
            case 0x22:
                //     0x22：温湿度探测模块
                if ((length < 11)) {
                    Toast.makeText(this, "温度，下位机上传错误数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                Double temperature = new Double(buffer[6] > 9 ? buffer[5] + "." + buffer[6] : buffer[5] + ".0" + buffer[6]);
                mDataBean.setTemperature(temperature);
                Double humidity = new Double(buffer[8] > 9 ? buffer[7] + "." + buffer[8] : buffer[7] + ".0" + buffer[8]);
                if (mDataBean.setHumidity(humidity)) {
                    return;
                }
                break;
            case 0x23:
                //     0x23：光照探测模块
                if ((length < 11)) {
                    Toast.makeText(this, "温度，下位机上传错误数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = "" + buffer[5] + (buffer[6] < 9 ? "0" + buffer[6] : buffer[6]) + (buffer[7] < 9 ? "0" + buffer[7] : buffer[7]);
                int light = Integer.parseInt(s);
                if (mDataBean.setSunLight(light)) {
                    return;
                }
                break;
            case 0x24:
                //     0x24：语音模块
                if (mDataBean.setSafe_voice(buffer[5])) {
                    return;
                }

                break;
            case 0x25:
                //     0x25: RFID读卡器

                break;
            case 0x31:
                //     0x31: 窗帘控制模块
                if (mDataBean.setCurrentState(buffer[5])) {
                    return;
                }
                break;
            case 0x32:
                //     0x32: PWM控制模块
                if (mDataBean.setLampLight(buffer[6])) {
                    return;
                }

                break;
            case 0x33:
                //     0x33: 电源控制模块
                if (mDataBean.setLampLight(buffer[6])) {
                    return;
                }

                break;
            case 0x34:
                // 0x34: 红外转发模块

                break;
            case 0x35:
                // 0x34: 键盘
                Intent intent = new Intent(this, AccessControl.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
        }

        receverDataFromServices(buffer);
    }

    private void sendData(byte[] bytes) {
        int leng = bytes.length;
        byte[] sendBytes = new byte[6 + leng];
        sendBytes[0] = 0x7f;
        sendBytes[1] = 0x66;
        sendBytes[2] = 0x20;
        sendBytes[3] = bytes[0];
        sendBytes[4] = (byte) (leng - 1);
        for (int i = 0; i < leng - 1; i++) {
            sendBytes[5 + i] = bytes[i + 1];
        }
        sendBytes[leng + 5] = 0x0d;
        sendBytes[leng + 6] = 0x0a;
    }

    //发送语音报警信号
    private void sendData(byte b2) {

        byte[] sendBytes = new byte[8];
        sendBytes[0] = 0x7f;
        sendBytes[1] = (byte) 0xAA;
        sendBytes[2] = 0x20;
        sendBytes[3] = 0x24;
        sendBytes[4] = 0x01;
        sendBytes[5] = b2;
        sendBytes[6] = 0x0d;
        sendBytes[7] = 0x0a;
        sendDataToServices(sendBytes);
    }

    private void receverDataFromServices(byte[] bytes) {
        String s = Util.byteToHexString(bytes);
        Log.e(TAG, "receverDataFromServices:bytes = " + s);
        EventBus.getDefault().post(new PostObj(bytes, Constant.RECIVE));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(PostObj postObj) {
        if (postObj.getSendOrRecive() == Constant.SEND) {
            sendDataToServices(postObj.getBytes());
        }
    }

    private void sendDataToServices(byte[] bytes) {
        Log.e(TAG, "sendDataToServices: " + Util.byte2String(bytes));
        synchronized (this) {
            //判断使用什么来通信：wifi:AA/串口:BB
            if (bytes[1] == 0xbb) {
                try {
                    mOutputStream.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                for (int i = 0; i < allOut.size(); i++) {
                    try {
                        allOut.get(i).write(bytes);
                    } catch (IOException e) {
                        //客户端断开连接
                        allOut.remove(i);
                        i--;
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInputStream = null;
        serving = false;
        EventBus.getDefault().register(this);
    }
}
