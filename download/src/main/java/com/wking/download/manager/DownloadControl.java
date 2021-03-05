package com.wking.download.manager;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * DownloadManager 下载暂停,开始控制器
 * 有两种方式,第一种是反射,第二种是ContentResolver
 *
 * @Author Sean
 * @Date 2021/3/3
 * @Description DownloadControl.java
 */
public class DownloadControl {
    private String TAG = "DownloadControl";
    public final String METHOD_NAME_PAUSE_DOWNLOAD = "pauseDownload";
    public final String METHOD_NAME_RESUME_DOWNLOAD = "resumeDownload";
    private static Method pauseDownload = null;
    private static Method resumeDownload = null;
    private DownloadManager mManager;
    private Context mContext;
    //默认应该使用该Uri.
    //private final Uri downloadUri = Uri.parse("content://downloads/my_downloads");

    public DownloadControl(Context context, DownloadManager manager) {
        this.mManager = manager;
        this.mContext = context;
    }

    /**
     * 通过反射恢复下载
     *
     * @param ids
     * @return
     */
    public boolean resume(long... ids) {
        if (resumeDownload == null) {
            try {
                resumeDownload = DownloadManager.class.getMethod(METHOD_NAME_RESUME_DOWNLOAD, long[].class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        try {
            int updatedRows = ((Integer) resumeDownload.invoke(mManager, ids)).intValue();
            return updatedRows > 0;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * 通过反射暂停下载
     *
     * @param ids
     * @return
     */
    public boolean pause(long... ids) {
        if (pauseDownload == null) {
            try {
                pauseDownload = DownloadManager.class.getMethod(METHOD_NAME_PAUSE_DOWNLOAD, long[].class);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
        }
        try {
            int updatedRows = ((Integer) pauseDownload.invoke(mManager, ids)).intValue();
            return updatedRows > 0;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }


    /**
     * 通过ContentResolver恢复下载
     *
     * @param localUri
     * @param id
     * @return
     */
    public boolean resume(Uri localUri, long id) {
        if (mContext == null || localUri == null) return false;
        int updatedRows = 0;
        ContentValues resumeDownload = new ContentValues();
        resumeDownload.put("control", 0); // Resume Control Value
        try {
            updatedRows = mContext
                    .getContentResolver()
                    .update(localUri,
                            resumeDownload,
                            DownloadManager.COLUMN_ID + "=?",
                            new String[]{id + ""});
        } catch (Exception e) {
            Log.e(TAG, "Failed to update control for downloading file." + e.getMessage());
        }
        return 0 < updatedRows;
    }

    /**
     * 通过ContentResolver暂停下载
     *
     * @param localUri
     * @param id
     * @return
     */
    public boolean pause(Uri localUri, long id) {
        if (mContext == null || localUri == null) return false;
        int updatedRows = 0;
        ContentValues pauseDownload = new ContentValues();
        pauseDownload.put("control", 1); // Pause Control Value
        try {
            updatedRows = mContext
                    .getContentResolver()
                    .update(localUri,
                            pauseDownload,
                            DownloadManager.COLUMN_ID + "=?",
                            new String[]{id + ""});
        } catch (Exception e) {
            Log.e(TAG, "Failed to update control for downloading file." + e.getMessage());
        }
        return 0 < updatedRows;
    }
}
