package com.gtafe.experimental.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gtafe.experimental.R;
import com.gtafe.experimental.utils.Util;

import butterknife.BindView;

/**
 * Created by ZhouJF on 2017/9/12.
 */

public class SafeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SafeActivity";

    @BindView(R.id.safe_gas)
    TextView mSafeGas;
    @BindView(R.id.safe_smoke)
    TextView mSafeSmoke;
    @BindView(R.id.safe_flame)
    TextView mSafeFlame;
    @BindView(R.id.safe_infrared)
    TextView mSafeInfrared;
    @BindView(R.id.safe_voice)
    TextView mSafeVoice;
    @BindView(R.id.safe_set)
    TextView mSafeSet;
    @BindView(R.id.safe_voice_img)
    ImageView mSafeVoiceImg;
    @BindView(R.id.environment_temp)
    TextView mEnvironmentTemp;
    @BindView(R.id.environment_wet)
    TextView mEnvironmentWet;


    @Override
    protected int setView() {
        return R.layout.activity_safe;
    }

    @Override
    protected void init() {

        mSafeSet.setOnClickListener(this);
        refreshView(null);
        if (mDataBean.isSafeState()) {
            mSafeSet.setText("已设防");
        } else {

            mSafeSet.setText("未设防");
        }
        mDataBean.setSafeState(!mDataBean.isSafeState());
    }

    private void refreshView(byte[] bytes) {
        switch (mDataBean.getSafe_voice()) {
            case 0xff:
                mSafeVoiceImg.setVisibility(View.GONE);
                break;
            case 1:
                mSafeVoiceImg.setVisibility(View.VISIBLE);
                mSafeVoice.setText("火警");
                break;
            case 2:
                mSafeVoiceImg.setVisibility(View.VISIBLE);
                mSafeVoice.setText("有人入侵");
                break;
            case 3:
                mSafeVoiceImg.setVisibility(View.VISIBLE);
                mSafeVoice.setText("烟雾报警");
                break;
            case 4:
                mSafeVoiceImg.setVisibility(View.VISIBLE);
                mSafeVoice.setText("燃气泄漏");
                break;
            case 5:
                mSafeVoiceImg.setVisibility(View.VISIBLE);
                mSafeVoice.setText("火警");
                break;
            case 6:
                break;
        }
        mSafeVoiceImg.setVisibility(mDataBean.getSafe_voice() == 0 ? View.GONE : View.VISIBLE);
        mEnvironmentTemp.setText("温度：" + mDataBean.getTemperature() + "℃");
        mEnvironmentWet.setText("湿度：" + mDataBean.getHumidity() + " %");
        refreshSafeState(mSafeGas, mDataBean.getSafe_gas());
        refreshSafeState(mSafeFlame, mDataBean.getSafe_flame());
        refreshSafeState(mSafeSmoke, mDataBean.getSafe_smoke());
        refreshSafeState(mSafeInfrared, mDataBean.getSafe_infrared());
    }

    private void refreshSafeState(TextView tv, int safe_gas) {
        switch (safe_gas) {
            case 0:
                tv.setText("未连接");
                tv.setTextColor(Color.BLACK);
                break;
            case 1:
                tv.getAnimation().cancel();
                tv.clearAnimation();
                tv.setText("正常");
                tv.setTextColor(Color.GREEN);
                break;
            case 2:
                tv.setText("报警");
                initAnimation(tv);
                tv.setTextColor(Color.RED);
                break;
        }
    }

    private void initAnimation(View view) {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 0.5f, 1, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(200);
        mScaleAnimation.setRepeatMode(Animation.REVERSE);
        mScaleAnimation.setRepeatCount(Animation.INFINITE);
        view.setAnimation(mScaleAnimation);
        mScaleAnimation.start();
    }

    @Override
    public void onClick(View view) {

        if (mDataBean.isSafeState()) {
            mSafeSet.setText("已设防");

        } else {
            byte[] sendBytes = new byte[8];

            sendBytes[0] = 0x7f;
            sendBytes[1] = (byte) 0xAA;
            sendBytes[2] = 0x20;
            sendBytes[3] = 0x24;
            sendBytes[4] = 0x01;
            sendBytes[5] = (byte) 0xff;
            sendBytes[6] = 0x0d;
            sendBytes[7] = 0x0a;
            sendDataToService(sendBytes);
            mSafeSet.setText("未设防");

        }
        mDataBean.setSafeState(!mDataBean.isSafeState());
    }

    @Override
    protected void recievDataFromServer(byte[] bytes) {
        super.recievDataFromServer(bytes);
        refreshView(bytes);
    }
}
