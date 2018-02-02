package com.gtafe.experimental.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gtafe.experimental.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ZhouJF on 2017/12/18.
 */

public class RecorderVideoListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "RecorderVideoListActivi";
    @BindView(R.id.recordvideolist_lv)
    ListView mRecordvideolistLv;
    public File[] mFiles;
    public Context mContext;
    public RecordvideolistAdapter mAdapter;
    public File mFile;
    public String mFilePath;

    @Override
    protected int setView() {
        return R.layout.activity_recordvideoactivity;
    }

    @Override
    protected void init() {
        mContext = this;
        mFilePath = getIntent().getStringExtra(MainActivity.FILEPATH);
        mAdapter = new RecordvideolistAdapter();
        mRecordvideolistLv.setAdapter(mAdapter);
        mRecordvideolistLv.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        mFile = null;
        mFile = new File(mFilePath);
        if (mFile.exists() && mFile.isDirectory()) {
            mFiles = null;
            mFiles = mFile.listFiles();
            Log.e(TAG, "refreshView: ");
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        File path = mFiles[position - 1];
        File[] files = path.listFiles();
        String mp4Path = null;
        if (!(files.length > 0)) {
            return;
        }
        for (File file : files) {
            Log.e(TAG, "newFile11: " + file.getName());
            if (file.getName().endsWith(".mp4")) {

                mp4Path = file.getAbsolutePath();
                break;
            }
        }
        if (files.length <= 2) {

            String replace = mp4Path.replace("mp4", "log" + System.currentTimeMillis());
            try {
                new File(replace).createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(mContext, VideoPlayerActivity.class);

        intent.putExtra("video_uri", mp4Path);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.recorder_delect_all)
    public void onViewClicked() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("是否删除所有留言？")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (File file : mFiles) {
                            if (file.isDirectory()) {
                                File[] files = file.listFiles();
                                for (File file1 : files) {
                                    file1.delete();
                                }
                                file.delete();
                            }

                            file.delete();
                        }
                        refreshView();
                    }

                });
        builder1.show();

    }

    class RecordvideolistAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mFiles == null) {
                return 1;
            }
            return mFiles.length + 1;
        }

        @Override
        public Object getItem(int position) {
            return mFiles[position - 1];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_recorderlist, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.setData(position);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.item_recorderliest_position)
            TextView mItemRecorderliestPosition;
            @BindView(R.id.item_recorderliest_data)
            TextView mItemRecorderliestData;
            @BindView(R.id.item_recorderliest_time)
            TextView mItemRecorderliestTime;
            @BindView(R.id.item_recorderliest_length)
            TextView mItemRecorderliestLength;
            @BindView(R.id.item_recorderliest_delect)
            TextView mItemRecorderliestDelect;

            private int mPosition;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
                mItemRecorderliestDelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext)
                                .setTitle("提示")
                                .setMessage("是否删除这条留言？")
                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File path = mFiles[mPosition - 1];
                                        File[] files = path.listFiles();
                                        for (File file : files) {
                                            file.delete();
                                        }
                                        path.delete();
                                        refreshView();
                                    }

                                });
                        builder1.show();
                    }
                });
            }

            public void setData(int position) {
                mPosition = position;
                if (position == 0) {
                    mItemRecorderliestPosition.setText("序号");
                    mItemRecorderliestData.setText("留言时间");
                    mItemRecorderliestTime.setText("时长");
                    mItemRecorderliestLength.setText("文件大小");
                    return;
                }
                mItemRecorderliestDelect.setVisibility(View.VISIBLE);
                File file = mFiles[position - 1];

                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files.length < 3) {
                        Log.e(TAG, "refreshView: " + position);
                        mItemRecorderliestPosition.setBackgroundResource(R.drawable.bg_new);
                    } else {
                        mItemRecorderliestPosition.setBackgroundResource(R.drawable.bg_new_nor);
                    }

                    File f = null;
                    for (File file1 : files) {
                        if (file1.getName().endsWith(".mp4")) {
                            f = file1;
                            break;
                        }
                    }
                    if (f != null) {
                        String name = f.getName();
                        String fileName = name.substring(0, name.indexOf("."));
                        mItemRecorderliestTime.setText(getDuration(f.getAbsolutePath()));
                        mItemRecorderliestPosition.setText(position + "");
                        mItemRecorderliestData.setText(DateFormat.format("MM-dd kk:mm:ss", Long.parseLong(fileName)));
                        mItemRecorderliestLength.setText((int) f.length() / 1024 + " kb");
                    }

                }
            }
        }
    }

    //获取MP4文件的时长
    private String getDuration(String pt) {
        String time = null;
        //Log.e(TAG, "getDuration: uri---"+Uri.parse(pt) );
        MediaPlayer mp = MediaPlayer.create(mContext, Uri.parse(pt));
        if (mp != null) {
            int duration = mp.getDuration();
            mp.release();
            duration /= 1000;
            if (duration < 60) {
                time = "00:00:" + transform(duration);
            } else if (duration >= 60 && duration < 60 * 60) {
                int min = duration / 60;
                time = "00:" + transform(min) + ":" + transform(duration % 60);
            } else {
                int hour = duration / 60 / 60;
                int min = 0;
                int sec = 0;
                if (duration - 3600 >= 60) {
                    min = (duration - 3600) / 60;
                    sec = (duration - 3600) % 60;
                } else if (duration - 3600 < 60) {
                    min = 00;
                    sec = duration - 3600;
                }
                time = transform(hour) + ":" + transform(min) + ":" + transform(sec);
            }
        }
        if (time == null) {
            time = "00:00:00";
        }
        return time;
    }

    private String transform(int i) {
        if (i >= 10) {
            return i + "";
        } else if (i < 10) {
            return "0" + i;
        }
        return null;
    }

}
