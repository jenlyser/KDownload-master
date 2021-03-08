package com.wking.download;


import android.util.Log;

public abstract class DownLoadProgressListener implements IDownloadListener {

    private String TAG = "DownLoadProgressListener";

    public DownLoadProgressListener() {
    }

    @Override
    public void onPause() {
        Log.d(TAG, "DownloadManager onPause");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "DownloadManager onPause");
    }

}
