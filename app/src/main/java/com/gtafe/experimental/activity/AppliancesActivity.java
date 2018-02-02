package com.gtafe.experimental.activity;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gtafe.experimental.R;
import com.xw.repo.BubbleSeekBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ZhouJF on 2017/9/12.
 */

public class AppliancesActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {
    private static final String TAG = "AppliancesActivity";

    @BindView(R.id.appliances_radiogroup)
    RadioGroup mAppliancesRadiogroup;
    @BindView(R.id.appliances_title)
    TextView mAppliancesTitle;

    @BindView(R.id.control_top_btn)
    ImageButton mControlTopBtn;
    @BindView(R.id.control_left_btn)
    ImageButton mControlLeftBtn;
    @BindView(R.id.control_ok_btn)
    ImageButton mControlOkBtn;
    @BindView(R.id.control_right_btn)
    ImageButton mControlRightBtn;
    @BindView(R.id.control_bottom_btn)
    ImageButton mControlBottomBtn;
    @BindView(R.id.control_ly)
    LinearLayout mPtzControlLy;
    @BindView(R.id.control_study)
    ImageView mControlStudy;
    @BindView(R.id.control_other1)
    ImageView mControlOther1;
    @BindView(R.id.control_other2)
    ImageView mControlOther2;
    @BindView(R.id.control_other3)
    ImageView mControlOther3;
    @BindView(R.id.appliances_remote)
    LinearLayout mAppliancesRemotecl;
    @BindView(R.id.control_power)
    ImageView mControlPower;
    @BindView(R.id.study)
    TextView mStudy;
    @BindView(R.id.control_a)
    TextView mControlA;
    @BindView(R.id.control_w)
    TextView mControlW;
    @BindView(R.id.control_chazuo)
    LinearLayout mControlChazuo;
    @BindView(R.id.control_other)
    LinearLayout mControlOther;
    @BindView(R.id.appliances_radiobutton_fengshan)
    RadioButton mAppliancesRadiobuttonFengshan;
    @BindView(R.id.appliances_radiobutton_dianshi)
    RadioButton mAppliancesRadiobuttonDianshi;
    @BindView(R.id.appliances_radiobutton_yinxiang)
    RadioButton mAppliancesRadiobuttonYinxiang;
    @BindView(R.id.appliances_radiobutton_chazuo)
    RadioButton mAppliancesRadiobuttonChazuo;
    @BindView(R.id.control_fengsu)
    TextView mControlFengsu;
    @BindView(R.id.fengsu_seekbar)
    BubbleSeekBar mFengsuSeekbar;
    @BindView(R.id.control_fengshu)
    LinearLayout mControlFengshu;
    @BindView(R.id.control_w_setting)
    EditText mControlWSetting;
    private byte[] mBytes;

    @Override
    protected int setView() {
        return R.layout.activity_appliances;
    }

    @Override
    protected void init() {
        initView();
        initByte();
    }

    private void initByte() {
        mBytes = new byte[9];
        mBytes[0] = 0x7f;
        mBytes[1] = 0x02;
        mBytes[2] = 0x20;
        mBytes[3] = 0x34;
        mBytes[4] = 0x02;
        mBytes[5] = (byte) appliances;
        mBytes[7] = 0x0d;
        mBytes[8] = 0x0a;
    }

    private void initView() {
        mControlWSetting.setText(mDataBean.getPowerLimit()+"");
        mControlWSetting.setOnFocusChangeListener(this);
        mAppliancesRadiogroup.setOnCheckedChangeListener(this);
        mAppliancesRadiogroup.check(mAppliancesRadiogroup.getChildAt(0).getId());
        mControlTopBtn.setOnTouchListener(mOnTouchListener);
        mControlOkBtn.setOnTouchListener(mOnTouchListener);
        mControlLeftBtn.setOnTouchListener(mOnTouchListener);
        mControlRightBtn.setOnTouchListener(mOnTouchListener);
        mControlBottomBtn.setOnTouchListener(mOnTouchListener);
        mFengsuSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                mControlFengsu.setText(progress + "");
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                byte[] bytes = new byte[10];
                bytes[0] = 0x7f;
                bytes[1] = (byte) 0x01;
                bytes[2] = 0x20;
                bytes[3] = 0x32;
                bytes[4] = 0x03;
                bytes[5] = 0x0C;
                bytes[6] = 0x01;
                bytes[7] = (byte) progress;
                bytes[8] = 0x0d;
                bytes[9] = 0x0a;
                sendDataToService(bytes);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }

    private int appliances = 0;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        TranslateAnimation translateanimation = null;
        ScaleAnimation scaleanimation = new ScaleAnimation(0.25f, 1, 0.25f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        switch (i) {
            case R.id.appliances_radiobutton_fengshan:
                appliances = 0;
              //  mControlFengshu.setVisibility(View.VISIBLE);
                mPtzControlLy.setVisibility(View.GONE);
                mControlOther.setVisibility(View.GONE);
                mControlChazuo.setVisibility(View.GONE);
                mAppliancesTitle.setText("智能风扇");
                translateanimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.8f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF, 0);
                break;

            case R.id.appliances_radiobutton_dianshi:
                appliances = 0x0B;
                mControlFengshu.setVisibility(View.GONE);
                mPtzControlLy.setVisibility(View.VISIBLE);
                mControlOther.setVisibility(View.VISIBLE);
                mControlChazuo.setVisibility(View.GONE);
                mAppliancesTitle.setText("智能电视");
                translateanimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.8f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -0.25f, Animation.RELATIVE_TO_SELF, 0);
                break;

            case R.id.appliances_radiobutton_yinxiang:
                appliances = 0x0A;
                mControlFengshu.setVisibility(View.GONE);
                mPtzControlLy.setVisibility(View.VISIBLE);
                mControlOther.setVisibility(View.VISIBLE);
                mControlChazuo.setVisibility(View.GONE);
                mAppliancesTitle.setText("智能音响");
                translateanimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.8f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.25f, Animation.RELATIVE_TO_SELF, 0);
                break;

            case R.id.appliances_radiobutton_chazuo:
                appliances = 3;
                mControlFengshu.setVisibility(View.GONE);
                mPtzControlLy.setVisibility(View.GONE);
                mControlOther.setVisibility(View.GONE);
                mControlChazuo.setVisibility(View.VISIBLE);
                mAppliancesTitle.setText("智能插座");
                translateanimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.8f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0);
                break;
        }
        AnimationSet animationSet = new AnimationSet(false);
        scaleanimation.setDuration(500);
        translateanimation.setDuration(500);
        animationSet.addAnimation(scaleanimation);
        animationSet.addAnimation(translateanimation);
        mAppliancesRemotecl.setAnimation(animationSet);
        animationSet.start();
    }


    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {

            int action = motionevent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    switch (view.getId()) {
                        case R.id.talkback_control_btn:
                            break;
                        case R.id.control_top_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_up_sel);
                            break;
                        case R.id.control_bottom_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bottom_sel);
                            break;
                        case R.id.control_left_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_left_sel);
                            break;
                        case R.id.control_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_right_sel);
                            break;
                        default:
                            break;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    switch (view.getId()) {
                        case R.id.talkback_control_btn:
                            break;
                        case R.id.control_ok_btn:
                            mBytes[6] = 0x05;
                            mBytes[5] = (byte) appliances;
                            sendDataToService(mBytes);
                            break;
                        case R.id.control_top_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mBytes[6] = 0x03;
                            mBytes[5] = (byte) appliances;
                            sendDataToService(mBytes);
                            break;
                        case R.id.control_bottom_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mBytes[6] = 0x07;
                            mBytes[5] = (byte) appliances;
                            sendDataToService(mBytes);
                            break;
                        case R.id.control_left_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mBytes[6] = 0x04;
                            mBytes[5] = (byte) appliances;
                            sendDataToService(mBytes);
                            break;
                        case R.id.control_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mBytes[6] = 0x06;
                            mBytes[5] = (byte) appliances;
                            sendDataToService(mBytes);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private boolean studding = false;

    @OnClick({R.id.control_study, R.id.control_other1, R.id.control_other2, R.id.control_other3, R.id.appliances_remote, R.id.control_power, R.id.study})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.control_study:

            case R.id.control_power:
                switch (appliances) {
                    case 0:
                        if (mDataBean.getFengshan() > 0) {
                            //风扇开着
                            byte[] bytes = new byte[10];
                            bytes[0] = 0x7f;
                            bytes[1] = 0x01;
                            bytes[2] = 0x20;
                            bytes[3] = 0x32;
                            bytes[4] = 0x03;
                            bytes[5] = 0x0C;
                            bytes[6] = 0x00;
                            bytes[7] = 0;
                            bytes[8] = 0x0d;
                            bytes[9] = 0x0a;
                            sendDataToService(bytes);
                        } else {
                            byte[] bytes = new byte[10];
                            bytes[0] = 0x7f;
                            bytes[1] = (byte) 0x01;
                            bytes[2] = 0x20;
                            bytes[3] = 0x32;
                            bytes[4] = 0x03;
                            bytes[5] = 0x0C;
                            bytes[6] = 0x01;
                            bytes[7] = 50;
                            bytes[8] = 0x0d;
                            bytes[9] = 0x0a;
                            sendDataToService(bytes);
                        }
                        break;
                    case 11:
                        //电视
                        mBytes[5] = (byte) appliances;
                        mBytes[6] = 0x02;
                        sendDataToService(mBytes);

                        break;
                    case 10:
                        //音响
                        mBytes[5] = (byte) appliances;
                        mBytes[6] = 0x02;
                        sendDataToService(mBytes);
                        break;
                    case 3:
                        if (mDataBean.getChazuoState() == 1) {
                            //已开启
                            byte[] bytes = new byte[8];
                            bytes[0] = 0x7f;
                            bytes[1] = (byte) 0x01;
                            bytes[2] = 0x20;
                            bytes[3] = 0x33;
                            bytes[4] = 0x01;
                            bytes[5] = 0x00;
                            bytes[6] = 0x0d;
                            bytes[7] = 0x0a;
                            sendDataToService(bytes);
                        } else {
                            byte[] bytes = new byte[8];
                            bytes[0] = 0x7f;
                            bytes[1] = 0x01;
                            bytes[2] = 0x20;
                            bytes[3] = 0x33;
                            bytes[4] = 0x01;
                            bytes[5] = 0x01;
                            bytes[6] = 0x0d;
                            bytes[7] = 0x0a;
                            sendDataToService(bytes);
                        }
                        break;
                }
                break;
            case R.id.control_other1:
                mBytes[6] = 0x08;
                mBytes[5] = (byte) appliances;
                sendDataToService(mBytes);
                break;
            case R.id.control_other2:
                mBytes[6] = 0x09;
                mBytes[5] = (byte) appliances;
                sendDataToService(mBytes);
                break;
            case R.id.control_other3:
                mBytes[6] = 0x0A;
                mBytes[5] = (byte) appliances;
                sendDataToService(mBytes);
                break;
            case R.id.appliances_remote:

                break;
        }
    }

    @Override
    protected void recievDataFromServer(byte[] bytes) {
        super.recievDataFromServer(bytes);
        if (bytes[3] == 0x33) {
            Log.e(TAG, "recievDataFromServer: 插座");
            mControlA.setText("电流：" + mDataBean.getElectricity() + "A");
            mControlW.setText("功率：" + mDataBean.getPower() + "W");

        }
        if (bytes[3] == 0x32) {
            if (bytes[5] == 0x0c) {
                mFengsuSeekbar.setProgress(mDataBean.getFengshan());
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String powerlimit = ((EditText) v).getText().toString().trim();
            if (TextUtils.isEmpty(powerlimit)) {
                Toast.makeText(mContext, "请输入限制最大功率值", Toast.LENGTH_SHORT).show();
                return;
            }
            Pattern p = Pattern .compile("^[1-9]\\d*$");
            Matcher m = p.matcher(powerlimit);
            if (m.matches()) {
                int power = Integer.parseInt(powerlimit);
                mDataBean.setPowerLimit(power);
                getSharedPreferences("powerlimit", MODE_PRIVATE).edit().putInt("powerlimit" ,power).commit();
            }else {
                Toast.makeText(mContext, "输入失败，最大功率值只能包含数字", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
