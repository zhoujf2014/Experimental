package com.gtafe.experimental.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

/**
 * Created by ZhouJF on 2018/1/12.
 */

public class RecordThread extends Thread {
    static final int frequency = 44100;
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private boolean playing  = true;


    @Override
    public void run() {
        // TODO Auto-generated method stub
        int recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding)*2;
        int plyBufSize = AudioTrack.getMinBufferSize(frequency,
                channelConfiguration, audioEncoding)*2;

        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                channelConfiguration, audioEncoding, recBufSize);

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
                channelConfiguration, audioEncoding, plyBufSize, AudioTrack.MODE_STREAM);

        byte[] recBuf = new byte[recBufSize];
        audioRecord.startRecording();
        audioTrack.play();
        while(playing){
            int readLen = audioRecord.read(recBuf, 0, recBufSize);
            audioTrack.write(recBuf, 0, readLen);
        }
        audioTrack.stop();
        audioRecord.stop();
    }
    public void stopPlay(){
        playing = false;
    }
}
