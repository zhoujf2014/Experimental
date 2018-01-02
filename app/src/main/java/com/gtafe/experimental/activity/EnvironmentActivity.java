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
    }

    private void refreshView() {
        mEnvironmentLight.setText("光照强度："+mDataBean.getSunLight() + "Lux");
        mEnvironmentTemp.setText("温度："+mDataBean.getTemperature() + "℃");
        mEnvironmentWet.setText("湿度："+mDataBean.getHumidity() + " %");
        mLightSeekbar.setProgress(mDataBean.getLampLight());

        mCurtentOppen.setText("开启");
        mCurtentOppen.setTextColor(Color.BLACK);
        mCurtentClose.setText("关闭");
        mCurtentClose.setTextColor(Color.BLACK);
        mCurtentStop.setText("停止");
        mCurtentStop.setTextColor(Color.BLACK);

        switch (mDataBean.getCurrentState()) {
            case 1:
                mCurtentOppen.setText("正在开启");
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
        switch (view.getId()) {
            case R.id.textView:
                break;
            case R.id.light_seekbar:
                break;
            case R.id.curtent_oppen:
                break;
            case R.id.curtent_stop:
                break;
            case R.id.curtent_close:
                break;
            case R.id.light_smartlight:
                break;
        }
    }
}
