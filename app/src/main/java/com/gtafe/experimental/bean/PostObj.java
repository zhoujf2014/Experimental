package com.gtafe.experimental.bean;

/**
 * Created by ZhouJF on 2017/9/15.
 */

public class PostObj {
    private byte[] mBytes;
    private int sendOrRecive;

    public PostObj(byte[] bytes, int sendOrRecive) {
        mBytes = bytes;
        this.sendOrRecive = sendOrRecive;
    }

    public byte[] getBytes() {
        return mBytes;
    }

    public void setBytes(byte[] bytes) {
        mBytes = bytes;
    }

    public int getSendOrRecive() {
        return sendOrRecive;
    }

    public void setSendOrRecive(int sendOrRecive) {
        this.sendOrRecive = sendOrRecive;
    }
}
