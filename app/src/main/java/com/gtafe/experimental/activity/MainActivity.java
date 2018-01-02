package com.gtafe.experimental.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gtafe.experimental.R;
import com.gtafe.experimental.utils.GlideImageLoader;
import com.videogo.EzvizApplication;
import com.videogo.ui.util.ActivityUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        Log.e(TAG, "onCreate: screenWidth=" + screenWidth + " screenHeigh=" + screenHeigh);
        initBanner();
        initGuangGao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecorderRideo();
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


            mRecorderCount.setText(files.length + "");
            if (files.length > 0) {
                mRecorderCount.setVisibility(View.VISIBLE);
                mRecorderCount.setBackgroundResource(R.drawable.bg_green_recorder);
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
            case R.id.monitoring:
                intent = new Intent(this, MonitoringActivity.class);
                break;
            /*    if (TextUtils.isEmpty(EzvizApplication.AppKey)) {
                    Toast.makeText(this, "Appkey为空", Toast.LENGTH_LONG).show();
                }
                //   EzvizApplication.getOpenSDK().startPushService();
                ActivityUtils.goToLoginAgain(this);
                //  intent = new Intent(getApplication(), EzvizWebViewActivity.class);
                //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //  intent.putExtra(IntentConsts.EXTRA_WEBVIEW_ACTION, EzvizWebViewActivity.WEBVIEW_ACTION_CLOUDPAGE);
                // startActivity(intent);
                return;*/
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


}
