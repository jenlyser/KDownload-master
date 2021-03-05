package com.wking.download;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

/**
 * 下载的文件状态监听
 *
 * @Author Sean
 * @Date 2021/3/4
 * @Description DownloadObserver.java
 */
class DownloadObserver extends ContentObserver {
    private String TAG = "DownloadObserver";
    private IDownloadQuery mQuery;//数据更新回调接口.
    private Handler mHandler;

    public DownloadObserver(Handler handler, IDownloadQuery query) {
        super(handler);
        this.mQuery = query;
        mHandler = handler;
    }

    /**
     * 继承接口
     *
     * @param selfChange
     */
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.d(TAG, "ContentObserver->onChange();");
        mHandler.postDelayed(() -> {
            notifyChanged();
        }, 1000);
    }

    /**
     * 通知下载文件有更改.回到查询.
     */
    private void notifyChanged() {
        if (mQuery != null) {
            mQuery.query();
        }
    }
}
