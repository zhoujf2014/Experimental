package com.gtafe.experimental.activity;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gtafe.experimental.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ZhouJF on 2017/9/12.
 */

public class AppliancesActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

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

    @Override
    protected int setView() {
        return R.layout.activity_appliances;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {
        mAppliancesRadiogroup.setOnCheckedChangeListener(this);
        mAppliancesRadiogroup.check(mAppliancesRadiogroup.getChildAt(0).getId());
        mControlTopBtn.setOnTouchListener(mOnTouchListener);
        mControlLeftBtn.setOnTouchListener(mOnTouchListener);
        mControlRightBtn.setOnTouchListener(mOnTouchListener);
        mControlBottomBtn.setOnTouchListener(mOnTouchListener);
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        TranslateAnimation translateanimation = null;
        ScaleAnimation scaleanimation = new ScaleAnimation(0.25f, 1, 0.25f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        switch (i) {
            case R.id.appliances_radiobutton_fengshan:
                mPtzControlLy.setVisibility(View.VISIBLE);
                mControlOther.setVisibility(View.VISIBLE);
                mControlChazuo.setVisibility(View.GONE);
                mAppliancesTitle.setText("智能风扇");
                translateanimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.8f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF, 0);
                break;

            case R.id.appliances_radiobutton_dianshi:
                mPtzControlLy.setVisibility(View.VISIBLE);
                mControlOther.setVisibility(View.VISIBLE);
                mControlChazuo.setVisibility(View.GONE);
                mAppliancesTitle.setText("智能电视");
                translateanimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.8f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -0.25f, Animation.RELATIVE_TO_SELF, 0);
                break;

            case R.id.appliances_radiobutton_yinxiang:
                mPtzControlLy.setVisibility(View.VISIBLE);
                mControlOther.setVisibility(View.VISIBLE);
                mControlChazuo.setVisibility(View.GONE);
                mAppliancesTitle.setText("智能音响");
                translateanimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.8f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.25f, Animation.RELATIVE_TO_SELF, 0);
                break;

            case R.id.appliances_radiobutton_chazuo:
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
                        case R.id.control_top_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            break;
                        case R.id.control_bottom_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            break;
                        case R.id.control_left_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            break;
                        case R.id.control_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
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
                if (studding) {
                    studding  =false;
                    mStudy.setText("学习功能");
                }else {
                    studding  =true;

                    mStudy.setText("正在学习");
                }
                break;
            case R.id.control_power:
                if (studding) {

                }else {

                }
                break;
            case R.id.control_other1:
                if (studding) {

                }else {

                }
                break;
            case R.id.control_other2:
                if (studding) {

                }else {

                }
                break;
            case R.id.control_other3:
                if (studding) {

                }else {

                }
                break;
            case R.id.appliances_remote:
                if (studding) {

                }else {

                }
                break;
        }
    }
}
