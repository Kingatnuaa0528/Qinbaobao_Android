package com.king.testandroidapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class TestService extends Service {

    public static final int MAX_PROGRESS = 100;
    private int progress = 0;
    private OnProgressListener onProgressListener;

    public int getProgress() {
        return progress;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public void startDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progress < MAX_PROGRESS) {
                    progress += 5;
                    onProgressListener.onProgress(progress);
                    try{
                        Thread.sleep(2000);
                    } catch (Exception e) {

                    }
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new TestBinder();
    }

    public class TestBinder extends Binder {
        public TestService getService() {
            return TestService.this;
        }
    }

    public interface OnProgressListener {
        void onProgress(int progress);
    }
}
