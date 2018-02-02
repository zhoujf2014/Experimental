package com.gtafe.experimental.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gtafe.experimental.R;
import com.gtafe.experimental.app.ExperimentalApplication;
import com.gtafe.experimental.bean.UserBean;
import com.gtafe.experimental.bean.UserBeanDao;
import com.gtafe.experimental.utils.Util;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhouJF on 2017/12/20.
 */

public class UserControlActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "UserControlActivity";
    @BindView(R.id.user_control_lv)
    ListView mUserControlLv;
    public List<UserBean> mUserBeens;
    public AlertDialog mDialog;
    public AddViewHolder mAddViewHolder;
    public UserBeanDao mUserBeanDao;
    public UserlistAdapter mUserlistAdapter;
    public int mFingerpiontid;

    @Override
    protected int setView() {
        return R.layout.activity_usercontrol;
    }

    @Override
    protected void init() {
        mUserBeanDao = ExperimentalApplication.getDaoSession().getUserBeanDao();
        mUserlistAdapter = new UserlistAdapter();
        mUserControlLv.setAdapter(mUserlistAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        mUserBeens = mUserBeanDao.loadAll();
        mUserlistAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.user_control_add)
    public void onViewClicked() {
        View mView = View.inflate(mContext, R.layout.dialog_settinguser_add, null);
        mAddViewHolder = new AddViewHolder(mView);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext).setView(mView).setCancelable(false);
        AlertDialog dialog = mBuilder.show();
        mAddViewHolder.setData(dialog, null);
    }

    @Override
    public void onClick(View v) {

    }

    class UserlistAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mUserBeens == null) {
                return 1;
            }
            return mUserBeens.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return mUserBeens.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_usercontrol, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.setData(position);
            return convertView;
        }


        class ViewHolder {
            @BindView(R.id.item_user_position)
            TextView mItemUserPosition;
            @BindView(R.id.item_user_name)
            TextView mItemUserName;
            @BindView(R.id.item_user_type)
            TextView mItemUserType;
            @BindView(R.id.item_user_password)
            TextView mItemUserPassword;
            @BindView(R.id.item_user_rfid)
            TextView mItemUserRfid;
            @BindView(R.id.item_user_fingerprint)
            TextView mItemUserFingerprint;
            @BindView(R.id.item_user_update)
            TextView mItemUserUpdate;
            @BindView(R.id.item_user_delect)
            TextView mItemUserDelect;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void setData(int position) {
                if (position == 0) {
                    mItemUserPosition.setText("序号");
                    mItemUserName.setText("用户名");
                    mItemUserType.setText("用户类型");
                    mItemUserPassword.setText("密码");
                    mItemUserRfid.setText("RFID");
                    mItemUserFingerprint.setText("指纹");
                    return;
                }
                final UserBean userBean = mUserBeens.get(position - 1);
                mItemUserPosition.setText(position + "");
                mItemUserName.setText(userBean.getName());
                mItemUserType.setText(userBean.getType());
                mItemUserPassword.setText(userBean.getPassword());
                mItemUserRfid.setText(userBean.getRfid());
                mItemUserFingerprint.setText(userBean.getFingerprint());
                mItemUserUpdate.setText("修改");
                mItemUserDelect.setText("删除");

                mItemUserUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View mView = View.inflate(mContext, R.layout.dialog_settinguser_add, null);
                        mAddViewHolder = new AddViewHolder(mView);
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext).setView(mView).setCancelable(false);
                        AlertDialog dialog = mBuilder.show();
                        mAddViewHolder.setData(dialog, userBean);
                    }
                });
                mItemUserDelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext)
                                .setTitle("提示")
                                .setMessage("是否删除用户 " + userBean.getName() + " ？")
                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mUserBeanDao.delete(userBean);
                                        dialog.dismiss();
                                        refreshView();
                                    }
                                });
                        builder1.show();

                    }
                });

            }
        }
    }

    class AddViewHolder implements View.OnClickListener {
        @BindView(R.id.textView2)
        TextView mTextView2;
        @BindView(R.id.dialog_settinguser_username)
        EditText mDialogSettinguserUsername;
        @BindView(R.id.dialog_settinguser_type)
        Spinner mDialogSettinguserType;
        @BindView(R.id.dialog_settinguser_enterpassword)
        EditText mDialogSettinguserEnterpassword;
        @BindView(R.id.dialog_settinguser_rfid)
        TextView mDialogSettinguserRfid;
        @BindView(R.id.dialog_settinguser_fingerpiont)
        TextView mDialogSettinguserFingerpiont;
        @BindView(R.id.dialog_settinguser_confir)
        Button mDialogSettinguserConfir;
        @BindView(R.id.dialog_settinguser_cancel)
        Button mDialogSettinguserCancel;
        private AlertDialog mAlertDialog;
        private UserBean addUserBean;
        /*  private String rfid = null;
            private String fingerPoint = null;*/

        private String rfid ;
        private String fingerPoint;

        AddViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setData(AlertDialog dialog, UserBean userBean) {
            mAlertDialog = dialog;
            if (userBean != null) {
                addUserBean = userBean;
                mDialogSettinguserUsername.setText(userBean.getName());
                mDialogSettinguserType.setSelection(userBean.getType().equals("管理员") ? 1 : 2);
                mDialogSettinguserEnterpassword.setText(userBean.getPassword());
                mDialogSettinguserRfid.setText("点击修改");
                mDialogSettinguserFingerpiont.setText("点击修改");
                rfid = userBean.getRfid();
                fingerPoint = userBean.getFingerprint();

            }

            mDialogSettinguserRfid.setOnClickListener(this);
            mDialogSettinguserFingerpiont.setOnClickListener(this);
            mDialogSettinguserConfir.setOnClickListener(this);
            mDialogSettinguserCancel.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_settinguser_rfid:
                    View waitView_rfid = View.inflate(mContext, R.layout.waiting_view_rfid, null);

                    AlertDialog.Builder mBuilder_rfid = new AlertDialog.Builder(mContext).setView(waitView_rfid).setCancelable(false)
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    mDialog = mBuilder_rfid.show();
                    leaning_rfid = true;

                    break;
                case R.id.dialog_settinguser_fingerpiont:
                    View waitView_fingerpiont = View.inflate(mContext, R.layout.waiting_view_fingerpoint, null);
                    AlertDialog.Builder mBuilder_fingerpiont = new AlertDialog.Builder(mContext).setView(waitView_fingerpiont).setCancelable(false)
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    mDialog = mBuilder_fingerpiont.show();
                    leaning_fingerPoint = true;
                    //发送指纹录入模式
                    SharedPreferences sp = getSharedPreferences("fingerpiont", MODE_PRIVATE);
                    mFingerpiontid = sp.getInt("fingerpiontid", 1);
                    if (mFingerpiontid == 100) {
                        mFingerpiontid = 1;
                    } else {
                        mFingerpiontid += 1;
                    }
                    sp.edit().putInt("fingerpiontid", mFingerpiontid).commit();

                    byte[] sendBytes = new byte[9];
                    sendBytes[0] = 0x7f;
                    sendBytes[1] = 0x01;
                    sendBytes[2] = 0x20;
                    sendBytes[3] = 0x0A;
                    sendBytes[4] = 0x02;
                    sendBytes[5] = 0X0A;
                    sendBytes[6] = (byte) mFingerpiontid;
                    sendBytes[7] = 0x0d;
                    sendBytes[8] = 0x0a;
                    sendDataToService(sendBytes);

                    break;
                case R.id.dialog_settinguser_confir:

                    String userName = mDialogSettinguserUsername.getText().toString().trim();
                    if (TextUtils.isEmpty(userName)) {
                        Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mDialogSettinguserType.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "请选择用户类型", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String type = (String) mDialogSettinguserType.getSelectedItem();
                    String password = mDialogSettinguserEnterpassword.getText().toString().trim();
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(mContext, "未输入密码", Toast.LENGTH_SHORT).show();

                    }
                    if (TextUtils.isEmpty(rfid)) {
                        Toast.makeText(mContext, "未录入RFID", Toast.LENGTH_SHORT).show();

                    }
                    if (TextUtils.isEmpty(fingerPoint)) {
                        Toast.makeText(mContext, "未录入指纹", Toast.LENGTH_SHORT).show();

                    }
                    if (addUserBean == null) {
                        addUserBean = new UserBean();
                        addUserBean.setId(System.currentTimeMillis());
                        addUserBean.setName(userName);
                        addUserBean.setType(type);
                        addUserBean.setPassword(password);
                        addUserBean.setRfid(rfid);
                        addUserBean.setFingerprint(fingerPoint);
                        mUserBeanDao.insert(addUserBean);
                    } else {
                        addUserBean.setName(userName);
                        addUserBean.setType(type);
                        addUserBean.setPassword(password);
                        addUserBean.setRfid(rfid);
                        addUserBean.setFingerprint(fingerPoint);
                        mUserBeanDao.update(addUserBean);
                    }
                    mAlertDialog.dismiss();
                    refreshView();
                    break;
                case R.id.dialog_settinguser_cancel:
                    mAlertDialog.dismiss();
                    break;
            }
        }

        public void setRfid(String s) {
            if (mDialog != null && mDialog.isShowing()) {
                rfid = s;
                mDialogSettinguserRfid.setText("已录入");
                mDialogSettinguserRfid.setTextColor(Color.GREEN);
                mDialog.dismiss();
            }
        }

        public void setFingerpoint(String s) {
            if (mDialog != null && mDialog.isShowing()) {
                fingerPoint = s;
                mDialogSettinguserFingerpiont.setText("已录入");
                mDialogSettinguserFingerpiont.setTextColor(Color.GREEN);
                mDialog.dismiss();
            }
        }
    }


    boolean leaning_rfid = false;
    boolean leaning_fingerPoint = false;

    @Override
    protected void recievDataFromServer(byte[] bytes) {
        super.recievDataFromServer(bytes);
        //接收到信号
        Log.e(TAG, "onMessageEvent:截取前data " + Util.byteToHexStringData(bytes));

        if (bytes[3] == 0x0A) {
            String s = null;
            switch (bytes[5]) {
                case -35:
                    //RFID
                    if (leaning_rfid) {
                        String s1 = Util.byteToHexStringData(bytes);
                        s = Util.byteToHexStringData(bytes).substring(12, 20);
                        Log.e(TAG, "onMessageEvent:截取前 " + s1 + "  截取后：" + s);
                        mAddViewHolder.setRfid(s);
                        leaning_rfid = false;
                    }
                    Log.e(TAG, "recievDataFromServerRFID: " + s);
                    break;
                case 0x0A:
                    //指纹

                    if (leaning_fingerPoint) {
                        String s5 = Util.byteToHexStringData(bytes);
                        Log.e(TAG, "onMessageEvent:截取前 " + s5 + "  截取后：" + s);

                        mAddViewHolder.setFingerpoint(mFingerpiontid + "");
                        leaning_fingerPoint = false;
                    }
                    Log.e(TAG, "recievDataFromServerFINGER: " + s);
                    break;
                case -18:
                    //密码
                    String s3 = Util.byteToHexStringData(bytes);
                    s = Util.byteToHexStringData(bytes).substring(12, 28);
                    Log.e(TAG, "onMessageEvent:截取前 " + s3 + "  截取后：" + s);
                    break;

            }

        }


    }


}
