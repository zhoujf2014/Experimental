package com.gtafe.experimental.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.light_setlight)
    Button mLightSetlight;
    @BindView(R.id.control_light)
    TextView mControlLight;
    @BindView(R.id.light_oppen)
    Button mLightOppen;
    @BindView(R.id.light_close)
    Button mLightClose;
    @BindView(R.id.light_control_ll)
    LinearLayout mLightControlLl;


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
                mControlLight.setText(progress + "");
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                byte[] bytes = new byte[10];
                bytes[0] = 0x7f;
                bytes[1] = (byte) 0x01;
                bytes[2] = 0x20;
                bytes[3] = 0x32;
                bytes[4] = 0x03;
                bytes[5] = 0x0B;
                bytes[6] = 0x01;
                bytes[7] = (byte) progress;
                bytes[8] = 0x0d;
                bytes[9] = 0x0a;
                if (progress==0){
                    bytes[6] = 0x00;
                }
                sendDataToService(bytes);
            }
            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
    }

    private void refreshView() {
        mEnvironmentLight.setText("光照强度：" + mDataBean.getSunLight() + "%");
        mEnvironmentTemp.setText("温度：" + mDataBean.getTemperature() + "℃");
        mEnvironmentWet.setText("湿度：" + mDataBean.getHumidity() + " %");
        //设置可调光亮度条
        mLightSeekbar.setProgress(mDataBean.getLampLight());
        boolean b = mDataBean.isSmartLightState();

        if (b) {
            mLightSmartlight.setText("智能控制中");
            mLightSmartlight.setTextColor(Color.GREEN);
            mLightControlLl.setVisibility(View.INVISIBLE);
        } else {
            mLightSmartlight.setText("手动控制");
            mLightSmartlight.setTextColor(Color.BLACK);

            mLightControlLl.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void recievDataFromServer(byte[] bytes) {
        super.recievDataFromServer(bytes);
        refreshView();
    }


    @OnClick({R.id.light_oppen, R.id.light_close, R.id.textView,  R.id.curtent_oppen, R.id.curtent_stop, R.id.curtent_close, R.id.light_smartlight, R.id.light_setlight})
    public void onViewClicked(View view) {
        byte[] bytes = new byte[8];
        bytes[0] = 0x7f;
        bytes[1] = (byte) 0x01;
        bytes[2] = 0x20;
        bytes[4] = 0x01;
        bytes[6] = 0x0d;
        bytes[7] = 0x0a;

        switch (view.getId()) {
            case R.id.light_oppen:
                bytes = new byte[10];
                bytes[0] = 0x7f;
                bytes[1] = (byte) 0x01;
                bytes[2] = 0x20;
                bytes[3] = 0x32;
                bytes[4] = 0x03;
                bytes[5] = 0x0A;
                bytes[6] = 0x01;
                bytes[7] = 100;
                bytes[8] = 0x0d;
                bytes[9] = 0x0a;

                break;
            case R.id.light_close:
                bytes = new byte[10];
                bytes[0] = 0x7f;
                bytes[1] = (byte) 0x01;
                bytes[2] = 0x20;
                bytes[3] = 0x32;
                bytes[4] = 0x03;
                bytes[5] = 0x0A;
                bytes[6] = 0x00;
                bytes[7] = 0;
                bytes[8] = 0x0d;
                bytes[9] = 0x0a;
                break;

            case R.id.curtent_oppen:
                bytes[3] = 0x31;
                bytes[5] = 0x1;
                break;
            case R.id.curtent_stop:
                bytes[3] = 0x31;
                bytes[5] = 0x02;
                break;
            case R.id.curtent_close:
                bytes[3] = 0x31;
                bytes[5] = 0x03;
                break;
            case R.id.light_smartlight:
                boolean b = !mDataBean.isSmartLightState();

                mDataBean.setSmartLightState(b);
                if (b) {
                    mLightSmartlight.setText("智能控制中");
                    mLightSmartlight.setTextColor(Color.GREEN);
                    mLightControlLl.setVisibility(View.INVISIBLE);
                } else {
                    mLightSmartlight.setText("手动控制");
                    mLightSmartlight.setTextColor(Color.BLACK);
                    mLightControlLl.setVisibility(View.VISIBLE);
                }
                return;
            case R.id.light_setlight:
                View dialog_smart = View.inflate(mContext, R.layout.dialog_smartlight_setting, null);
                final EditText smartLightLimit_L = (EditText) dialog_smart.findViewById(R.id.smartlight_limit_l);
                final EditText smartLightLimit_H = (EditText) dialog_smart.findViewById(R.id.smartlight_limit_h);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("智能灯设置")
                        .setView(dialog_smart)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //    dialog.dismiss();

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String limit_L = smartLightLimit_L.getText().toString().trim();
                                String limit_H = smartLightLimit_H.getText().toString().trim();
                                if (TextUtils.isEmpty(limit_H) || TextUtils.isEmpty(limit_L)) {
                                    Toast.makeText(mContext, "输入的值不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                int limitH = Integer.parseInt(limit_H);
                                int limitL = Integer.parseInt(limit_L);
                                if (limitH < limitL) {
                                    Toast.makeText(mContext, "打开值不能大于关闭值！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                getSharedPreferences("smartLightLimit", MODE_PRIVATE).edit().putInt("limith", limitH).putInt("limitl", limitL).commit();
                                mDataBean.setSmartLightH(limitH);
                                mDataBean.setSmartLightL(limitL);
                                // dialog.dismiss();
                            }
                        })
                        .setCancelable(true);
                smartLightLimit_L.setText(mDataBean.getSmartLightL() + "");
                smartLightLimit_H.setText(mDataBean.getSmartLightH() + "");
                builder.show();
                return;


        }
        sendDataToService(bytes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
