package com.gtafe.experimental.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.gtafe.experimental.bean.DaoMaster;
import com.gtafe.experimental.bean.DaoSession;
import com.gtafe.experimental.bean.DataBean;
import com.gtafe.experimental.server.SocketService;
import com.videogo.openapi.EZOpenSDK;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;

import static com.gtafe.experimental.Constant.Constant.ENVIRONMENT_SMARTCURTAIN_ISAUTO;
import static com.gtafe.experimental.Constant.Constant.EXPERIMENTAL;
import static com.gtafe.experimental.Constant.Constant.LIGHT_SMARTLIGHT_ISAUTO;

/**
 * Created by ZhouJF on 2017/9/15.
 */

public class ExperimentalApplication extends Application {
    private static DataBean mDataBean;
    public static Context mContext;
    public static SerialPort mSerialPort;
    public static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initHK();
        initData();
        startService();

    }

    /**
     * 设置greenDao
     */
    private static void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, "notes-db", null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        if (mDaoSession == null) {
            setDatabase();
        }
        return mDaoSession;
    }

    private void initHK() {
        EZOpenSDK.showSDKLog(true);
        EZOpenSDK.enableP2P(true);
        EZOpenSDK.initLib(this, "57fe9b6b3dca44ff9ab32be33006a868", "");
    }

    private void initData() {

      /*boolean light_smartlight_isauto = mContext.getSharedPreferences(EXPERIMENTAL, MODE_PRIVATE).getBoolean(LIGHT_SMARTLIGHT_ISAUTO, false);
        mDataBean.setSmartLightState(light_smartlight_isauto);
       boolean environment_smartcurtain_isauto = mContext.getSharedPreferences(EXPERIMENTAL, MODE_PRIVATE).getBoolean(ENVIRONMENT_SMARTCURTAIN_ISAUTO, false);
        mDataBean.setCurtainAuto(environment_smartcurtain_isauto);

        mDataBean.setSafe_gas(2);
        mDataBean.setSafe_infrared(2);
        mDataBean.setSafe_flame(1);*/
    }

    public static DataBean getDataBean() {
        if (mDataBean==null){
            synchronized (ExperimentalApplication.class){
                if (mDataBean==null) {
                    mDataBean = new DataBean();
                }
            }
        }
        return mDataBean;
    }

    private void startService() {
        Intent service = new Intent(this, SocketService.class);
        startService(service);
    }

    public static SerialPort getSerialPort() {

        if (mSerialPort == null) {

            try {
                // mSerialPort = new SerialPort(new File("/dev/ttySAC1"), 115200, 0);
                mSerialPort = new SerialPort(new File("/dev/ttyS2"), 115200, 0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mSerialPort;
    }


}
