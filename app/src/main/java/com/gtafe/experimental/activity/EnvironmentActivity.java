package com.gtafe.experimental.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gtafe.experimental.R;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhouJF on 2017/9/12.
 */

public class EnvironmentActivity extends BaseActivity {
    private static final String TAG = "EnvironmentActivity";

    @BindView(R.id.environment_light)
    TextView mEnvironmentLight;
    @BindView(R.id.environment_temp)
    TextView mEnvironmentTemp;
    @BindView(R.id.environment_wet)
    TextView mEnvironmentWet;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.light_seekbar)
    BubbleSeekBar mLightSeekbar;
    @BindView(R.id.curtent_oppen)
    Button mCurtentOppen;
    @BindView(R.id.curtent_stop)
    Button mCurtentStop;
    @BindView(R.id.curtent_close)
    Button mCurtentClose;
    @BindView(R.id.light_smartlight)
    Button mLightSmartlight;


    @Override
    protected int setView() {
        return R.layout.activity_environment;
    }

    @Override
    protected void init() {
        refreshView();
        mLightSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {


            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }

    private void refreshView() {
        mEnvironmentLight.setText("光照强度：" + mDataBean.getSunLight() + "Lux");
        mEnvironmentTemp.setText("温度：" + mDataBean.getTemperature() + "℃");
        mEnvironmentWet.setText("湿度：" + mDataBean.getHumidity() + " %");
        mLightSeekbar.setProgress(mDataBean.getLampLight());

        mCurtentOppen.setText("打开");
        mCurtentOppen.setTextColor(Color.BLACK);
        mCurtentClose.setText("关闭");
        mCurtentClose.setTextColor(Color.BLACK);
        mCurtentStop.setText("停止");
        mCurtentStop.setTextColor(Color.BLACK);

        switch (mDataBean.getCurrentState()) {
            case 1:
                mCurtentOppen.setText("正在打开");
                mCurtentOppen.setTextColor(Color.GREEN);
                break;
            case 2:

                mCurtentClose.setText("正在关闭");
                mCurtentClose.setTextColor(Color.GREEN);
                break;
            case 3:
                mCurtentStop.setText("已停止");
                mCurtentStop.setTextColor(Color.GREEN);
                break;
            case 0:
                mCurtentOppen.setText("未连接");
                mCurtentOppen.setTextColor(Color.RED);
                mCurtentClose.setText("未连接");
                mCurtentClose.setTextColor(Color.RED);
                mCurtentStop.setText("未连接");
                mCurtentStop.setTextColor(Color.RED);
                break;
        }
    }

    @Override
    protected void recievDataFromServer(byte[] bytes) {
        super.recievDataFromServer(bytes);
        refreshView();
    }


    @OnClick({R.id.textView, R.id.light_seekbar, R.id.curtent_oppen, R.id.curtent_stop, R.id.curtent_close, R.id.light_smartlight})
    public void onViewClicked(View view) {
        byte[] bytes = new byte[8];
        bytes[0] = 0x7f;
        bytes[1] = (byte) 0xBB;
        bytes[2] = 0x20;
        bytes[6] = 0x0d;
        bytes[7] = 0x0a;


        switch (view.getId()) {

            case R.id.curtent_oppen:
                bytes[3] = 0x31;
                bytes[4] = 0x1;
                break;
            case R.id.curtent_stop:
                bytes[3] = 0x31;
                bytes[4] = 0x03;
                break;
            case R.id.curtent_close:
                bytes[3] = 0x31;
                bytes[4] = 0x02;
                break;
            case R.id.light_smartlight:

           /*    bytes = new byte[10];
                bytes[0] = 0x7f;
                bytes[1] = (byte) 0xBB;
                bytes[2] = 0x20;
                bytes[3] = 0x32;
                bytes[4] = 0x01;
                bytes[5] = 0x0B;
                if (mDataBean.isSmartLightState()) {

                    bytes[6] = 0x01;
                }
                bytes[7] = 0x00;
                bytes[8] = 0x0d;
                bytes[9] = 0x0a;*/
                return;
        }
        sendDataToService(bytes);
    }
}
