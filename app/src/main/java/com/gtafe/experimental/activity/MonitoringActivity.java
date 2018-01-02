package com.gtafe.experimental.activity;


import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.gtafe.experimental.R;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.util.LogUtil;
import java.util.List;
import butterknife.BindView;

/**
 * Created by ZhouJF on 2017/9/12.
 */

public class MonitoringActivity extends BaseActivity implements SurfaceHolder.Callback {
    private static final String TAG = "MonitoringActivity";
    @BindView(R.id.video_real)
    SurfaceView mVideoReal;
    @BindView(R.id.ptz_top_btn)
    ImageButton mPtzTopBtn;
    @BindView(R.id.ptz_left_btn)
    ImageButton mPtzLeftBtn;
    @BindView(R.id.ptz_flip_btn)
    ImageButton mPtzFlipBtn;
    @BindView(R.id.ptz_right_btn)
    ImageButton mPtzRightBtn;
    @BindView(R.id.ptz_bottom_btn)
    ImageButton mPtzBottomBtn;
    @BindView(R.id.ptz_control_ly)
    LinearLayout mPtzControlLy;

    public SurfaceHolder mSurfaceHolder;
    public EZOpenSDK mEZOpenSDK;
    public EZCameraInfo mCameraInfo;
    public EZPlayer mEZPlayer;
    public Handler mHandler;


    @Override
    protected int setView() {
        return R.layout.activity_monitoring;
    }

    @Override
    protected void init() {
        initData();
        initView();
    }

    private void initView() {
        mSurfaceHolder = mVideoReal.getHolder();
        mSurfaceHolder.addCallback(this);
        mPtzTopBtn.setOnTouchListener(mOnTouchListener);
        mPtzLeftBtn.setOnTouchListener(mOnTouchListener);
        mPtzRightBtn.setOnTouchListener(mOnTouchListener);
        mPtzBottomBtn.setOnTouchListener(mOnTouchListener);
        mPtzControlLy.setOnTouchListener(mOnTouchListener);
        mPtzFlipBtn.setOnTouchListener(mOnTouchListener);
    }

    private void initData() {

        EZOpenSDK.getInstance().setAccessToken("at.7nfuwyup9qbgh8a116ro8c604vui4pqx-5j1cfmunpz-01aimew-yvacr0jil");


        mEZOpenSDK = EZOpenSDK.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EZDeviceInfo> deviceList = mEZOpenSDK.getDeviceList(0, 10);
                    EZDeviceInfo ezDeviceInfo = deviceList.get(0);
                    mCameraInfo = ezDeviceInfo.getCameraInfoList().get(0);
                    mEZPlayer = EZOpenSDK.getInstance().createPlayer(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo());
                    // mVerifyCode = DataManager.getInstance().getDeviceSerialVerifyCode(mCameraInfo.getDeviceSerial());
                    // Log.e(TAG, "run: "+mVerifyCode +" mCameraInfo.getDeviceSerial()="+mCameraInfo.getDeviceSerial());
                    mEZPlayer.setPlayVerifyCode("VPDOIB");
                    mEZPlayer.setHandler(mHandler);
                    mEZPlayer.setSurfaceHold(mSurfaceHolder);
                    mEZPlayer.startRealPlay();

                } catch (BaseException e) {
                    int errorCode = e.getErrorCode();
                    Log.e(TAG, "run: " + errorCode + "");
                }
            }
        }).start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(holder);
        }
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(null);
        }
        mSurfaceHolder = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EZOpenSDK.getInstance().setAccessToken(null);
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            int action = motionevent.getAction();
            final int speed = EZConstants.PTZ_SPEED_DEFAULT;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.e(TAG, "onTouch: down_viewID=" + view.getId());
                    switch (view.getId()) {
                        case R.id.ptz_top_btn:
                            mPtzFlipBtn.setVisibility(View.VISIBLE);
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_up_sel);
                            // setPtzDirectionIv(RealPlayStatus.PTZ_UP);
                            Log.e(TAG, "onTouch: EZPTZCommandUp=" + view.getId());
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_bottom_btn:
                            mPtzFlipBtn.setVisibility(View.VISIBLE);
                            Log.e(TAG, "onTouch: EZPTZCommandDown=" + view.getId());
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bottom_sel);
                            // setPtzDirectionIv(RealPlayStatus.PTZ_DOWN);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_left_btn:
                            mPtzFlipBtn.setVisibility(View.VISIBLE);
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_left_sel);
                            //setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.ptz_right_btn:
                            mPtzFlipBtn.setVisibility(View.VISIBLE);
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_right_sel);
                            // setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;

                        default:
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e(TAG, "onTouch: 00up_viewID=" + view.getId());
                    switch (view.getId()) {
                        case R.id.ptz_top_btn:
                        case R.id.ptz_bottom_btn:
                        case R.id.ptz_left_btn:
                        case R.id.ptz_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            mPtzFlipBtn.setVisibility(View.INVISIBLE);
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    return true;
            }
            return false;
        }
    };

    private void ptzOption(final EZConstants.EZPTZCommand command, final EZConstants.EZPTZAction action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ptz_result = false;
                try {
                    ptz_result = mEZOpenSDK.controlPTZ(mCameraInfo.getDeviceSerial(), mCameraInfo.getCameraNo(), command,
                            action, EZConstants.PTZ_SPEED_FAST);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                LogUtil.i(TAG, "controlPTZ ptzCtrl result: " + ptz_result);
            }
        }).start();
    }


}