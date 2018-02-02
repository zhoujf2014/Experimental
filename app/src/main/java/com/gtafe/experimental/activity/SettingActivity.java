package com.gtafe.experimental.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gtafe.experimental.R;

import butterknife.BindView;


/**
 * Created by ZhouJF on 2017/12/22.
 */

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SettingActivity";
    @BindView(R.id.setting_rfid)
    CheckBox mSettingRfid;
    @BindView(R.id.setting_fingerpoint)
    CheckBox mSettingFingerpoint;
    @BindView(R.id.setting_password)
    CheckBox mSettingPassword;
    public SharedPreferences mSharedPreferences;


    @Override
    protected int setView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        mSharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        mSettingRfid.setChecked(mSharedPreferences.getBoolean("rfid", false));
        mSettingPassword.setChecked(mSharedPreferences.getBoolean("password", false));
        mSettingFingerpoint.setChecked(mSharedPreferences.getBoolean("fingerpoint", false));
        mSettingRfid.setOnCheckedChangeListener(this);
        mSettingFingerpoint.setOnCheckedChangeListener(this);
        mSettingPassword.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setting_rfid:
                mSharedPreferences.edit().putBoolean("rfid", isChecked).commit();
                Log.e(TAG, "onCheckedChanged: rfid");
                break;
            case R.id.setting_password:
                mSharedPreferences.edit().putBoolean("password", isChecked).commit();

                Log.e(TAG, "onCheckedChanged: setting_password");

                break;
            case R.id.setting_fingerpoint:
                mSharedPreferences.edit().putBoolean("fingerpoint", isChecked).commit();

                Log.e(TAG, "onCheckedChanged: setting_fingerpoint");

                break;
        }
    }
}
