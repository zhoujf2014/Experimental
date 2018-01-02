package com.gtafe.experimental.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhouJF on 2017/12/20.
 */

public class UserControlActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.user_control_lv)
    ListView mUserControlLv;
    public List<UserBean> mUserBeens;
    public AlertDialog mDialog;
    public AddViewHolder mAddViewHolder;
    public UserBeanDao mUserBeanDao;
    public UserlistAdapter mUserlistAdapter;

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
                                .setMessage("是否删除用户 "+userBean.getName()+" ？")
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

        private String rfid = "666";
        private String fingerPoint = "666";

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
                        Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(rfid)) {
                        Toast.makeText(mContext, "请录入RFID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(fingerPoint)) {
                        Toast.makeText(mContext, "请输录入指纹", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (addUserBean==null){
                        addUserBean = new UserBean();
                        addUserBean.setId(System.currentTimeMillis());
                        addUserBean.setName(userName);
                        addUserBean.setType(type);
                        addUserBean.setPassword(password);
                        addUserBean.setRfid(rfid);
                        addUserBean.setFingerprint(fingerPoint);
                        mUserBeanDao.insert(addUserBean);
                    }else {
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
            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                rfid = s;
                mDialogSettinguserRfid.setText("已录入");
                mDialogSettinguserRfid.setTextColor(Color.GREEN);
                mAlertDialog.dismiss();
            }
        }

        public void setFingerpoint(String s) {
            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                fingerPoint = s;
                mDialogSettinguserFingerpiont.setText("已录入");
                mDialogSettinguserFingerpiont.setTextColor(Color.GREEN);
                mAlertDialog.dismiss();
            }
        }
    }


    boolean leaning_rfid = true;
    boolean leaning_fingerPoint = true;

    @Override
    protected void recievDataFromServer(byte[] bytes) {
        super.recievDataFromServer(bytes);
        //接收到信号
        String s = "666";
        if (leaning_rfid) {
            mAddViewHolder.setRfid(s);
            leaning_rfid = false;
            leaning_fingerPoint = false;
            //  mAddViewHolder.setFingerpoint(s);
        }


    }
}
