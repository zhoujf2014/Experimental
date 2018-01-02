package com.gtafe.experimental.activity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.gtafe.experimental.Constant.Constant;
import com.gtafe.experimental.R;
import com.gtafe.experimental.bean.PostObj;
import com.xw.repo.BubbleSeekBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gtafe.experimental.Constant.Constant.EXPERIMENTAL;
import static com.gtafe.experimental.Constant.Constant.LIGHT_SEEKBAR_PROGRESS;
import static com.gtafe.experimental.Constant.Constant.LIGHT_SMARTLIGHT_STATE;

/**
 * Created by ZhouJF on 2017/9/12.
 */

public class LightActivity extends BaseActivity {
    private static final String TAG = "LightActivity";


    @BindView(R.id.light_seekbar)
    BubbleSeekBar mLightSeekbar;
    @BindView(R.id.light_smartlight)
    Button mLightSmartlight;
    @BindView(R.id.light_text)
    TextView mLightText;
    private boolean smartLightState ;
    @Override
    protected int setView() {
        return R.layout.activity_light;
    }

    @Override
    protected void init() {
        initSeekBar();
        initSmartLight();//
    }

    private void initSmartLight() {
       smartLightState = getSharedPreferences(EXPERIMENTAL, MODE_PRIVATE).getBoolean(LIGHT_SMARTLIGHT_STATE, false);
        mLightSmartlight.setText(smartLightState?"开启":"关闭");
        mLightSmartlight.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,smartLightState?R.drawable.light_smart_oppen:R.drawable.light_smart_close,0);

    }

    private void initSeekBar() {
        int progress = getSharedPreferences(EXPERIMENTAL, MODE_PRIVATE).getInt(LIGHT_SEEKBAR_PROGRESS, 0);
        mLightText.setText(progress + "");
        mLightSeekbar.setProgress(progress);
        mLightSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                mLightText.setText(progress + "");
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                Log.e(TAG, "getProgressOnActionUp: ");
                getSharedPreferences(EXPERIMENTAL, MODE_PRIVATE).edit().putInt(LIGHT_SEEKBAR_PROGRESS, progress).commit();

                //TODO
                sendByteToService(new byte[1024]);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }

    private void sendByteToService(byte[] bytes) {

        PostObj postObj = new PostObj(bytes, Constant.SEND);
        EventBus.getDefault().post(postObj);
    }



    @OnClick(R.id.light_smartlight)
    public void onViewClicked() {
        smartLightState = !smartLightState;
        getSharedPreferences(EXPERIMENTAL,MODE_PRIVATE).edit().putBoolean(LIGHT_SMARTLIGHT_STATE,smartLightState).commit();
        mLightSmartlight.setText(smartLightState?"开启":"关闭");
        mLightSmartlight.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,smartLightState?R.drawable.light_smart_oppen:R.drawable.light_smart_close,0);

    }

}
