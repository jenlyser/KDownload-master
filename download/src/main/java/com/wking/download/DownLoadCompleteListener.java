package com.wking.download;


import android.util.Log;

/**
 * 监听下载完成虚拟类,部分情况,不需要监听下载进度,仅仅需要监听下载完成功能
 *
 * @Author Sean
 * @Date 2021 /3/11
 * @Description DownLoadCompleteListener.java
 */
public abstract class DownLoadCompleteListener implements IDownloadListener {

    private String TAG = "DownLoadCompleteListener";

    /**
     * Instantiates a new Down load complete listener.
     */
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
