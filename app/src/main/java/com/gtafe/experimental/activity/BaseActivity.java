package com.gtafe.experimental.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.gtafe.experimental.Constant.Constant;
import com.gtafe.experimental.R;
import com.gtafe.experimental.app.ExperimentalApplication;
import com.gtafe.experimental.bean.DataBean;
import com.gtafe.experimental.bean.PostObj;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by ZhouJF on 2017/9/15.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected DataBean mDataBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        mDataBean = ExperimentalApplication.getDataBean();
        mContext = this;
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        init();
    }

    protected abstract int setView();

    protected abstract void init();

    protected void sendDataToService(byte[] bytes) {
        EventBus.getDefault().post(new PostObj(bytes, Constant.SEND));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PostObj postObj) {
        if (postObj.getSendOrRecive() == Constant.RECIVE) {
            recievDataFromServer(postObj.getBytes());
        }
    }

    //接收到信息 判断处理
    protected void recievDataFromServer(byte[] bytes) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private float downY;
    private float upY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                upY = event.getY();
                if (upY - downY > 300 || upY - downY < -300) {
                    downY = 0;
                    upY = 0;
                    finish();
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
