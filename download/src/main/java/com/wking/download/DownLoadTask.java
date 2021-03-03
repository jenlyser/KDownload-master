package com.wking.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.wking.download.manager.DownloadControl;
import com.wking.download.manager.TaskHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * DownloadManager 任务管理
 *
 * @Author Sean
 * @Date 2021 /3/3
 * @Description DownLoadTask.java
 */
public class DownLoadTask implements IDownLoadTask {
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
                mInfo.setDownloadSizeBytes(cur.getInt(
                        cur.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
                mInfo.setTotalSizeBytes(cur.getInt(
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
                            cur.getColumnIndex((Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
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
    }

    /**
     * uri 转换成 Uri
     *
     * @param uri
     * @return
     */
    private Uri getUri(String uri) {
        return Uri.parse(uri);
    }


    /**
     * 添加监听
     *
     * @param listener 监听接口
     */
    @Override
    public void registerListener(IDownLoadListener listener) {
        synchronized (mListener) {
            if (listener != null && !mListener.contains(listener))
                mListener.add(listener);
        }
    }

    /**
     * 移除监听
     *
     * @param listener
     */
    @Override
    public void unregisterListener(IDownLoadListener listener) {
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
        return mDownloadControl.pause(mDownloadId);
    }

    /**
     * 恢复下载
     *
     * @return
     */
    @Override
    public boolean resume() {
        initControl();
        return mDownloadControl.resume(mDownloadId);
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
        //从下载记录中移除.
        TaskHolder.getInstance().remove(mDownloadId);
        dispose();
        return cancel > 0;
    }

    public void dispose(){

    }

}
