package com.wking.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.wking.download.manager.DownloadControl;

import java.util.ArrayList;
import java.util.List;

/**
 * DownloadManager 任务管理
 *
 * @Author Sean
 * @Date 2021 /3/3
 * @Description DownLoadTask.java
 */
public class DownLoadTask implements IDownLoadTask, IDownloadQuery {
    /**
     * The constant TAG.
     */
    public static String TAG = "DownLoadUtils";
    private DownloadManager mManager;
    private Context mContext;
    private DownloadInfo mInfo;
    private DownloadControl mDownloadControl;
    private long mDownloadId;
    private List<IDownLoadListener> mListener = new ArrayList<>();
    private DownloadObserver mDownloadObserver;
    private IDownloadCancel mCancel;
    private final Uri downloadUri = Uri.parse("content://downloads/my_downloads");

    /**
     * Instantiates a new Down load task.
     *
     * @param context    the context
     * @param manager    the manager
     * @param downloadId the download id
     */
    public DownLoadTask(Context context, DownloadManager manager, long downloadId) {
        this.mManager = manager;
        this.mContext = context;
        this.mDownloadId = downloadId;
        getDownLoadInfo();//初始化数据
    }

    /**
     * 初始化控制
     */
    private void initControl() {
        if (mDownloadControl == null) {
            mDownloadControl = new DownloadControl(mContext, mManager);
        }
    }

    @Override
    public void query() {
        query(mManager, mDownloadId, true);
    }

    /**
     * 查询数据状态
     *
     * @param manager
     * @param downloadId
     * @param isProgress
     */
    private void query(DownloadManager manager, long downloadId, boolean isProgress) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        if (mInfo == null) {
            mInfo = new DownloadInfo();
            isProgress = false;
        }
        Cursor cur = null;
        try {
            cur = manager.query(query);
            if (cur != null && cur.moveToFirst()) {
                mInfo.setReason(cur.getInt(
                        cur.getColumnIndex(DownloadManager.COLUMN_REASON)));
                mInfo.setStatus(cur.getInt(
                        cur.getColumnIndex(DownloadManager.COLUMN_STATUS)));
                mInfo.setDownloadSizeBytes(cur.getLong(
                        cur.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
                mInfo.setTotalSizeBytes(cur.getLong(
                        cur.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)));
                if (!isProgress) {
                    mInfo.setId(cur.getLong(
                            cur.getColumnIndex(DownloadManager.COLUMN_ID)));
                    mInfo.setTitle(cur.getString(
                            cur.getColumnIndex(DownloadManager.COLUMN_TITLE)));
                    mInfo.setDescription(cur.getString(
                            cur.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION)));
                    mInfo.setLastModifiedTimestamp(cur.getLong(
                            cur.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
                    mInfo.setUri(getUri(
                            cur.getString(
                                    cur.getColumnIndex(DownloadManager.COLUMN_URI))));
                    String localUri = cur.getString(
                            cur.getColumnIndex((Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB
                                    ? DownloadManager.COLUMN_LOCAL_URI
                                    : DownloadManager.COLUMN_LOCAL_FILENAME)));
                    mInfo.setLocalUri(
                            getUri(localUri));
                    mInfo.setMediaProviderUri(getUri(
                            cur.getString(
                                    cur.getColumnIndex(DownloadManager.COLUMN_MEDIAPROVIDER_URI))));
                    mInfo.setMediaType(cur.getString(
                            cur.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)));
                }

            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        //只有监测进度时才会回调
        if (isProgress) {
            //暂停时会反复回调.
            if (!mInfo.isPaused()) {
                onDownloadProgress();
            }
            if (mInfo.isComplete()) {
                onDownloadComplete();
            }
        }
    }

    /**
     * uri 转换成 Uri
     *
     * @param uri
     * @return
     */
    private Uri getUri(String uri) {
        if (TextUtils.isEmpty(uri)) return null;
        return Uri.parse(uri);
    }

    /**
     * 设置取消监听
     *
     * @param cancel
     */
    void setCancelListen(IDownloadCancel cancel) {
        this.mCancel = cancel;
    }

    /**
     * 添加监听
     *
     * @param listener 监听接口
     */
    @Override
    public void registerListener(IDownLoadListener listener) {
        Log.d(TAG,"registerListener");
        synchronized (mListener) {
            if (listener != null && !mListener.contains(listener))
                mListener.add(listener);
        }
        //如果注册监听前就已经完成了,需要异常调用进度并且完成方法.
        if (getDownLoadInfo().isComplete()) {
            Log.d(TAG,"isComplete");
            onDownloadProgress();
            onDownloadComplete();
        } else {
            //注册文件监听
            if (mDownloadObserver == null) {
                mDownloadObserver = new DownloadObserver(new Handler(Looper.getMainLooper()), this);
                register();
            }
        }
    }

    /**
     * 移除监听
     *
     * @param listener
     */
    @Override
    public void unregisterListener(IDownLoadListener listener) {
        Log.d(TAG,"unregisterListener");
        synchronized (mListener) {
            if (listener != null && mListener.contains(listener))
                mListener.remove(listener);
        }
    }

    /**
     * 获取下载信息
     *
     * @return
     */
    @Override
    public DownloadInfo getDownLoadInfo() {
        query(mManager, mDownloadId, false);
        return mInfo;
    }

    /**
     * 暂停下载
     *
     * @return
     */
    @Override
    public boolean pause() {
        initControl();
//        boolean status = mDownloadControl.pause(mDownloadId);
        boolean status = mDownloadControl.pause(downloadUri, mDownloadId);
        if (status) {
            onDownloadPause();
        }
        return status;
    }

    /**
     * 恢复下载
     *
     * @return
     */
    @Override
    public boolean resume() {
        initControl();
//        boolean status = mDownloadControl.resume(mDownloadId);
        boolean status = mDownloadControl.resume(downloadUri, mDownloadId);
        if (status) {
            onDownloadResume();
        }
        return status;
    }

    /**
     * 取消下载
     *
     * @return
     */
    @Override
    public boolean cancel() {
        //从DownloadManager移除.
        int cancel = mManager.remove(mDownloadId);
        if (mCancel != null) {
            mCancel.onCancel(mDownloadId);
        }
        //释放数据
        dispose();
        return cancel > 0;
    }

    /**
     * 注册监听文件内容更改.
     */
    private void register() {
        if (mDownloadObserver != null) {
            mContext.getContentResolver().registerContentObserver(downloadUri, true, mDownloadObserver);
        }
    }

    /**
     * 取消文件内容监听
     */
    private void unregister() {
        if (mDownloadObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mDownloadObserver);
        }
    }


    /**
     * 恢复现在事件
     */
    private void onDownloadResume() {
        if (mListener == null || mListener.size() < 1) return;
        Log.d(TAG, "onDownloadResume");
        synchronized (mListener) {
            for (int i = 0; i < mListener.size(); i++) {
                mListener.get(i).onResume();
            }
        }
    }

    /**
     * 暂停下载事件
     */
    private void onDownloadPause() {
        if (mListener == null || mListener.size() < 1) return;
        Log.d(TAG, "onDownloadPause");
        synchronized (mListener) {
            for (int i = 0; i < mListener.size(); i++) {
                mListener.get(i).onPause();
            }
        }
    }

    /**
     * 下载进度通知
     */
    private void onDownloadProgress() {
        if (mListener == null || mListener.size() < 1) return;
        Log.d(TAG, "onDownloadProgress");
        synchronized (mListener) {
            for (int i = 0; i < mListener.size(); i++) {
                mListener.get(i).onProgress(mInfo.getProgress(), mInfo);
            }
        }
    }

    /**
     * 下载完成通知
     */
    private void onDownloadComplete() {
        if (mListener == null || mListener.size() < 1) return;
        Log.d(TAG, "onDownloadComplete");
        synchronized (mListener) {
            for (int i = 0; i < mListener.size(); i++) {
                mListener.get(i).onComplete(mInfo.isSuccessful(), mInfo);
            }
        }
    }

    /**
     * Dispose.
     */
    public void dispose() {
        unregister();
        mListener.clear();
    }

}
