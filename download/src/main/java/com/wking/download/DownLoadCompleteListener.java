package com.wking.download;


import android.util.Log;

public abstract class DownLoadCompleteListener implements IDownloadListener {

    private String TAG = "DownLoadCompleteListener";

    public DownLoadCompleteListener() {
    }

    @Override
    public void onPause() {
        Log.d(TAG, "DownloadManager onPause");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "DownloadManager onPause");
    }

    @Override
    public void onProgress(double percent, DownloadInfo info) {
        Log.d(TAG, "DownloadManager onProgress:" + percent + "%");

    }

}
