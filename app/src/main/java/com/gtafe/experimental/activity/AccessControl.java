package com.gtafe.experimental.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.gtafe.experimental.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccessControl extends BaseActivity implements SurfaceHolder.Callback {
    @BindView(R.id.oppen)
    Button mOppen;
    @BindView(R.id.refues)
    Button mRefues;
    private SurfaceView mView;
    public SurfaceHolder mHolder;
    private static final String TAG = "ChrisAcvitity";
    private Camera mCamera;


    @Override
    protected int setView() {
        return R.layout.activity_accesscontrol;
    }

    @Override
    protected void init() {
        mView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mView.getHolder();
        mHolder.addCallback(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBean.setCalling(false);
        //我来自笔记本
        //我来自电脑
        //我来自电脑AS
        //我来自笔记本ASs
        //我来自电脑AS1111111111111
//        Intent intent = new Intent();
//        intent.setAction("acom.gtafe.testcamera.restartLAUNCHER");
//        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == mCamera) {
            mCamera = getCameraInstance();
            if (mCamera == null) {
                return;
            }
            try {
                mHolder.addCallback(this);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            if (mCamera == null) {
                return;
            }
            mHolder.removeCallback(this);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            Log.e(TAG, "surface:surfaceDestroyed: ");
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surface:surfaceCreated: ");

        if (null == mCamera) {
            mCamera = getCameraInstance();
            if (mCamera == null) {
                return;
            }
            try {
                mHolder.addCallback(this);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera(); // 这一步是否多余？在以后复杂的使用场景下，此步骤是必须的。
        int rotation = getDisplayOrientation(); //获取当前窗口方向
        if (mCamera == null) {
            return;
        }
        mCamera.setDisplayOrientation(rotation); //设定相机显示方向
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            if (mCamera == null) {
                return;
            }
            mHolder.removeCallback(this);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            Log.e(TAG, "surface:surfaceDestroyed: ");
        }
    }

    private void refreshCamera() {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        if (mCamera == null) {
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }


    // 获取当前窗口管理器显示方向
    private int getDisplayOrientation() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 270;
                break;
            case Surface.ROTATION_90:
                degrees = 0;
                break;
            case Surface.ROTATION_180:
                degrees = 90;
                break;
            case Surface.ROTATION_270:
                degrees = 180;
                break;
        }

        Camera.CameraInfo camInfo =
                new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        // 这里其实还是不太懂：为什么要获取camInfo的方向呢？相当于相机标定？？
        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }

    // 获取camera实例
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.d("TAG", "camera is not available");
        }
        return c;
    }

    public boolean hideNavigation() {
        boolean ishide;
        try {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib service call activity 42 s16 com.android.systemui";
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            proc.waitFor();
            ishide = true;
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ishide = false;
        }
        return ishide;
    }

    public boolean showNavigation() {
        boolean isshow;
        try {
            String command;
            command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService";
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            proc.waitFor();
            isshow = true;
        } catch (Exception e) {
            isshow = false;
            e.printStackTrace();
        }
        return isshow;
    }

    @OnClick({R.id.oppen, R.id.refues})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oppen:

                finish();
                break;
            case R.id.refues:

                finish();
                break;
        }
    }

}
