package com.gtafe.experimental.activity;

import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.gtafe.experimental.Constant.Constant;
import com.gtafe.experimental.R;
import com.gtafe.experimental.utils.RecordThread;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccessControl extends BaseActivity implements SurfaceHolder.Callback {
    @BindView(R.id.oppen)
    Button mOppen;
    @BindView(R.id.refues)
    Button mRefues;
    private SurfaceView mView;
    public SurfaceHolder mHolder;
    private static final String TAG = "ChrisAcvitity";
    private Camera mCamera;
    public RecordThread mRecordThread;
    public AlertDialog mDialog;


    @Override
    protected int setView() {
        return R.layout.activity_accesscontrol;
    }

    @Override
    protected void init() {
        mView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mView.getHolder();
        mHolder.addCallback(this);
        setRetime();
    }

    private boolean isClose = true;

    private void setRetime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isClose) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("提示").setMessage("通话将在5秒后结束，点击取消将继续通话")
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isClose = false;
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isClose = true;
                                        dialog.dismiss();
                                    }
                                });
                        mDialog = builder.show();

                    }
                });
                for (int i = 0; i < 6; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.setMessage("通话将在" + (5 - finalI) + "秒后结束，点击取消可以继续通话");
                            if (finalI == 5) {
                                if (isClose) {
                                    finish();

                                }
                            }
                        }
                    });


                }

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBean.setCalling(false);
        isClose = false;
        //我来自笔记本
        //我来自电脑
        //我来自电脑AS
        //我来自笔记本ASs
        //我来自电脑AS1111111111111
//        Intent intent = new Intent();
//        intent.setAction("acom.gtafe.testcamera.restartLAUNCHER");
//        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecordThread = new RecordThread();
        mRecordThread.start();
        if (null == mCamera) {
            mCamera = getCameraInstance();
            if (mCamera == null) {
                return;
            }
            try {
                mHolder.addCallback(this);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecordThread.stopPlay();
        if (mCamera != null) {
            if (mCamera == null) {
                return;
            }
            mHolder.removeCallback(this);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            Log.e(TAG, "surface:surfaceDestroyed: ");
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surface:surfaceCreated: ");

        if (null == mCamera) {
            mCamera = getCameraInstance();
            if (mCamera == null) {
                return;
            }
            try {
                mHolder.addCallback(this);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。
        //  int rotation = getDisplayOrientation(); //获取当前窗口方向
        int rotation = Constant.ROTATION;
        if (mCamera == null) {
            return;
        }
        mCamera.setDisplayOrientation(rotation); //设定相机显示方向
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            if (mCamera == null) {
                return;
            }
            mHolder.removeCallback(this);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            Log.e(TAG, "surface:surfaceDestroyed: ");
        }
    }

    private void refreshCamera() {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        if (mCamera == null) {
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }


    // 获取当前窗口管理器显示方向
    private int getDisplayOrientation() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 90;
                break;
            case Surface.ROTATION_90:
                degrees = 180;
                break;
            case Surface.ROTATION_180:
                degrees = 270;
                break;
            case Surface.ROTATION_270:
                degrees = 0;
                break;
        }

        Camera.CameraInfo camInfo =
                new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        // 这里其实还是不太懂：为什么要获取camInfo的方向呢？相当于相机标定？？
        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }

    // 获取camera实例
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.d("TAG", "camera is not available");
        }
        return c;
    }

    public boolean hideNavigation() {
        boolean ishide;
        try {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui";
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            proc.waitFor();
            ishide = true;
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ishide = false;
        }
        return ishide;
    }

    public boolean showNavigation() {
        boolean isshow;
        try {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService";
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            proc.waitFor();
            isshow = true;
        } catch (Exception e) {
            isshow = false;
            e.printStackTrace();
        }
        return isshow;
    }

    @OnClick({R.id.oppen, R.id.refues})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oppen:
                //开锁
                byte[] bytes = new byte[10];
                bytes[0] = 0x7f;
                bytes[1] = 0x01;
                bytes[2] = 0x20;
                bytes[3] = 0x0A;
                bytes[4] = 0x02;
                bytes[5] = 0x1A;
                bytes[6] = 0x01;
                bytes[7] = 0x0d;
                bytes[8] = 0x0a;

                sendDataToService(bytes);
                finish();
                break;
            case R.id.refues:

                finish();
                break;
        }
    }

}
