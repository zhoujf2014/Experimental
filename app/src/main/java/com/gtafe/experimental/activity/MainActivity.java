package com.gtafe.experimental.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gtafe.experimental.Constant.Constant;
import com.gtafe.experimental.R;
import com.gtafe.experimental.app.ExperimentalApplication;
import com.gtafe.experimental.bean.PostObj;
import com.gtafe.experimental.bean.UserBean;
import com.gtafe.experimental.bean.UserBeanDao;
import com.gtafe.experimental.utils.GlideImageLoader;

import com.gtafe.experimental.utils.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.util.DeviceUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String GUANGGAOURL = "https://raw.githubusercontent.com/zhoujf2014/downloadAPP/master/guanggao.txt";
    public static final String FILEPATH = "filepath";
    @BindView(R.id.monitoring)
    LinearLayout mMonitoring;
    @BindView(R.id.light)
    LinearLayout mLight;
    @BindView(R.id.appliances)
    LinearLayout mAppliances;
    @BindView(R.id.environment)
    LinearLayout mEnvironment;
    @BindView(R.id.safe)
    LinearLayout mSafe;
    @BindView(R.id.expand)
    LinearLayout mExpand;
    @BindView(R.id.main_banner)
    Banner mMainBanner;
    @BindView(R.id.xlb)
    ImageView mXlb;
    @BindView(R.id.tb)
    TextView mTb;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.guanggao)
    TextView mGuanggao;
    public File mFile;
    @BindView(R.id.recorder_count)
    TextView mRecorderCount;
    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        Log.e(TAG, "onCreate: screenWidth=" + screenWidth + " screenHeigh=" + screenHeigh);
        initBanner();
       // initGuangGao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecorderRideo();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int layoutDirection = newConfig.getLayoutDirection();
    }

    private void initRecorderRideo() {
        String path = null;
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                path = dcim + "/gtafe";
            } else {
                path = dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/gtafe";
            }
        } else {
            path = dcim + "/gtafe";
        }

        mFile = new File(path);
        if (mFile.exists() && mFile.isDirectory()) {
            File[] files = mFile.listFiles();


            if (files.length > 0) {
                mRecorderCount.setVisibility(View.VISIBLE);
                mRecorderCount.setBackgroundResource(R.drawable.bg_green_recorder);


                for (int i = 0; i < files.length; i++) {
                    boolean save = false;
                    if (!files[i].isDirectory()) {
                        files[i].delete();
                        i--;
                        continue;
                    }
                    File[] files1 = files[i].listFiles();
                    for (int i1 = 0; i1 < files1.length; i1++) {
                        if (files1[i1].getName().contains(".mp4")) {
                            save = true;
                        }
                    }
                    if (!save) {
                        for (int i1 = 0; i1 < files1.length; i1++) {
                            files1[i1].delete();
                            i1--;
                        }
                        files[i].delete();
                        i--;
                    }
                }

                files = mFile.listFiles();
                mRecorderCount.setText(files.length + "");
                for (File file : files) {
                    if (file.isDirectory()) {
                        File[] files1 = file.listFiles();
                        if (files1.length <= 2) {
                            mRecorderCount.setBackgroundResource(R.drawable.bg_red_recorder);
                            break;
                        }
                    }
                }
            } else {
                mRecorderCount.setVisibility(View.GONE);
            }
        }
    }

    private void initGuangGao() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(GUANGGAOURL).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                byte[] bytes = new byte[2048];
                int read = inputStream.read(bytes);
                if (read < 1) {
                    return;
                }
                final String guanggao = new String(bytes, 0, read, "utf-8");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mGuanggao.setText(guanggao);
                    }
                });
            }
        });

    }


    private void initBanner() {
        mMainBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<Integer> images = new ArrayList<>();
    /*    images.add(R.drawable.vp1);
        images.add(R.drawable.vp2);*/
        images.add(R.drawable.vp3);
        mMainBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        //设置banner动画效果
        mMainBanner.setBannerAnimation(Transformer.DepthPage);

        //设置自动轮播，默认为true
        mMainBanner.isAutoPlay(true);
        //设置轮播时间
        mMainBanner.setDelayTime(30000);
        //设置指示器位置（当banner模式中有指示器时）
        mMainBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mMainBanner.start();
    }

    @OnClick({R.id.monitoring, R.id.light, R.id.appliances, R.id.environment, R.id.safe, R.id.expand, R.id.recorder_count})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.light:
                intent = new Intent(this, LightActivity.class);
                break;
            case R.id.appliances:
                intent = new Intent(this, AppliancesActivity.class);
                break;
            case R.id.environment:
                intent = new Intent(this, EnvironmentActivity.class);
                break;
            case R.id.safe:
                intent = new Intent(this, SafeActivity.class);
                break;
            case R.id.expand:
                intent = new Intent(this, ExpandActivity.class);
                break;
            case R.id.recorder_count:
                intent = new Intent(this, RecorderVideoListActivity.class);
                intent.putExtra(FILEPATH, mFile.getAbsolutePath());
                break;
        }
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PostObj postObj) {
        if (postObj.getSendOrRecive() == Constant.RECIVE) {
            byte[] bytes = postObj.getBytes();
            if (bytes[3] != 0x0A) {
                return;
            }
            int command = bytes[5];
            Log.e(TAG, "onMessageEvent:  " + Util.byte2String(bytes));

            switch (command) {
                case -29:
                    //呼叫
                    Intent intent = new Intent(this, AccessControl.class);
                    startActivity(intent);
                    break;
                case -32:
                    //开始留言
                    unStop = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (!starting) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MediaRecorderActivity.goSmallVideoRecorder(MainActivity.this, VideoPlayerActivity.class.getName(), null);
                                        starting = true;
                                    }
                                });
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (starting) {
                                Intent broacast = new Intent("com.gtafe.experimental_recorder");
                                broacast.putExtra("conmmon", 1);
                                sendBroadcast(broacast);
                            }


                        }
                    }).start();
                    break;
                case -31:
                    //结束留言
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            unStop = false;
                            Intent broacast = new Intent("com.gtafe.experimental_recorder");
                            broacast.putExtra("conmmon", 2);
                            sendBroadcast(broacast);
                            starting = false;

                        }
                    }).start();

                    break;
                case -35:
                    //RFID
                    String s1 = Util.byteToHexStringData(bytes);
                    String s = Util.byteToHexStringData(bytes).substring(12, 20);

                    List<UserBean> userBeens = ExperimentalApplication.getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Rfid.eq(s)).build().list();
                    if (userBeens != null && userBeens.size() > 0) {
                        Log.e(TAG, "onMessageEvent:查询到" + userBeens.get(0).getName());
                        showDialog(userBeens.get(0).getName(), true);
                    } else {
                        showDialog("验证失败", false);

                    }

                    break;
                case -18:
                    //密码
                    String s3 = Util.byteToHexStringData(bytes);
                    char a = (char) bytes[6];
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < 8; i++) {
                        if (bytes[6 + i] == 0) {
                            break;
                        } else {
                            stringBuffer.append((char) bytes[6 + i]);
                        }
                    }

                    List<UserBean> userBeens2 = ExperimentalApplication.getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Password.eq(stringBuffer.toString())).build().list();
                    if (userBeens2 != null && userBeens2.size() > 0) {

                        Log.e(TAG, "onMessageEvent:查询到" + userBeens2.get(0).getName());
                        showDialog(userBeens2.get(0).getName(), true);
                    } else {
                        showDialog("验证失败", false);
                    }

                    break;
                case -52:
                    //指纹

                    String s5 = Util.byteToHexStringData(bytes);
                    Log.e(TAG, "onMessageEvent指纹:截取前 " + s5);
                    List<UserBean> userBeens1 = ExperimentalApplication.getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.Fingerprint.eq(bytes[6] + "")).build().list();
                    if (userBeens1 != null && userBeens1.size() > 0) {

                        Log.e(TAG, "onMessageEvent:查询到" + userBeens1.get(0).getName());
                        showDialog(userBeens1.get(0).getName(), true);
                    } else {
                        showDialog("验证失败", false);
                    }

                    break;
            }
        }
    }

    private boolean unStop = false;
    private boolean starting = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void showDialog(String s, boolean checkState) {
        String message = null;
        final int icon;
        if (checkState) {
            icon = R.drawable.pass;
            message = "你好，" + s + "!    祝您生活愉快！";
            oppenDorp();
        } else {
            icon = R.drawable.pass_error;
            message = "验证失败,换个姿势再试一下！";
        }

        final String finalMessage = message;
        final int finalIcon = icon;
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(
                                MainActivity.this,
                                finalMessage,
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        LinearLayout toastView = (LinearLayout) toast.getView();
                        ImageView imageCodeProject = new ImageView(
                                getApplicationContext());
                        imageCodeProject.setImageResource(finalIcon);
                        toastView.addView(imageCodeProject, 0);
                        toast.show();
                    }
                });
            }
        }).start();
    }

    private void oppenDorp() {
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

        EventBus.getDefault().post(new PostObj(bytes, Constant.SEND));
    }


    private void checkID() {
        mSharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);

        mSharedPreferences.getBoolean("rfid", false);
        mSharedPreferences.getBoolean("password", false);
        mSharedPreferences.getBoolean("fingerpoint", false);
    }

}