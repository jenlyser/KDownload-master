package com.wking.download;


import android.util.Log;

/**
 * 监听下载进度类,一般暂停和恢复的时候可以忽略,而且外部方法导致的暂停和恢复难以监听
 *
 * @Author Sean
 * @Date 2021 /3/11
 * @Description DownloadProgressListener.java
 */
public abstract class DownloadProgressListener implements IDownloadListener {

    private String TAG = "DownLoadProgressListener";

    /**
     * Instantiates a new Download progress listener.
     */
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
