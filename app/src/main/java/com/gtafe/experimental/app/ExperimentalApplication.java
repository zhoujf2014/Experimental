package com.gtafe.experimental.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gtafe.experimental.bean.DaoMaster;
import com.gtafe.experimental.bean.DaoSession;
import com.gtafe.experimental.bean.DataBean;
import com.gtafe.experimental.server.SocketService;


import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;


/**
 * Created by ZhouJF on 2017/9/15.
 */

public class ExperimentalApplication extends Application {
    private static final String TAG = "ExperimentalApplication";
    private static DataBean mDataBean;
    public static Context mContext;
    public static SerialPort mSerialPort;
    public static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

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


    private void initData() {
        int powerLimit = getSharedPreferences("powerlimit", MODE_PRIVATE).getInt("powerlimit", 600);

        SharedPreferences sp = getSharedPreferences("smartLightLimit", MODE_PRIVATE);
        int limith = sp.getInt("limith", 40);
        int limitl = sp.getInt("limitl", 5);

        DataBean dataBean = getDataBean();
        dataBean.setPowerLimit(powerLimit);
        dataBean.setSmartLightH(limith);
        dataBean.setSmartLightL(limitl);
    }

    public static DataBean getDataBean() {
        if (mDataBean == null) {
            synchronized (ExperimentalApplication.class) {
                if (mDataBean == null) {
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

    public static SerialPort getSerialPort() throws SecurityException, IOException {


        // mSerialPort = new SerialPort(new File("/dev/ttySAC1"), 115200, 0);
        return mSerialPort = new SerialPort(new File("/dev/ttyUSB0"), 115200, 0);


    }
}
