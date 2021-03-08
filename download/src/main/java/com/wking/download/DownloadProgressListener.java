package com.wking.download;


import android.util.Log;

public abstract class DownloadProgressListener implements IDownloadListener {

    private String TAG = "DownLoadProgressListener";

    public DownloadProgressListener() {
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
