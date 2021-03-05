package com.wking.download;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;

import com.wking.download.manager.TaskHolder;
import com.wking.download.manager.TaskModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Download manager pro.
 */
public class DownloadManagerPro {

    //全局上下文
    private static Context mContext;
    //DownloadManager对象
    private static DownloadManager mManager;
    //DownloadManager任务列表
    private static List<DownLoadTask> mDownloadList = new ArrayList<>();
    private static IDownloadCancel mDownloadCancel;//取消任务时从列表里删除

    /**
     * Sets context.
     *
     * @param context the context
     */
    public static void setContext(Context context) {
        DownloadManagerPro.mContext = context;
    }

    /**
     * Gets manager.
     *
     * @return the manager
     */
    public static DownloadManager getManager() {
        if (mManager == null) {
            mManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return mManager;
    }

    /**
     * get Context
     *
     * @return
     */
    private static Context getContext() {
        if (mContext == null) {
            try {
                mContext = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mContext == null) {
            try {
                mContext = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mContext;
    }

    /**
     * 添加下载任务
     *
     * @param request the request
     * @return the down load task
     */
    public static DownLoadTask addTask(DownloadManager.Request request) {
        if (request != null) {
            long downLoadId = getManager().enqueue(request);
            DownLoadTask task = new DownLoadTask(getContext(), getManager(), downLoadId);
            addTaskToList(task);
            saveTask(task.getDownLoadInfo());
            return task;
        }
        return null;
    }

    /**
     * 获取当前下载的所有任务
     *
     * @return tasks tasks
     */
    public static List<DownLoadTask> getTasks() {
        if (mDownloadList == null) {
            mDownloadList = new ArrayList<>();
        }
        if (mDownloadList.size() < 1) {
            synchronized (mDownloadList) {
                List<TaskModel> taskModels = TaskHolder.getInstance().getDataList();
                for (int i = 0; i < taskModels.size(); i++) {
                    addTaskToList(new DownLoadTask(getContext(), getManager(), taskModels.get(i).getId()));
                }
            }
        }
        return mDownloadList;
    }

    /**
     * 添加任务
     *
     * @param task
     */
    private static void addTaskToList(DownLoadTask task) {
        if (mDownloadCancel == null) {
            mDownloadCancel = new IDownloadCancel() {
                @Override
                public void onCancel(long id) {
                    //从下载记录中移除.
                    TaskHolder.getInstance().remove(id);
                    DownLoadTask downLoadTask = getTaskByDownloadId(id, null);
                    if (downLoadTask != null && mDownloadList.contains(downLoadTask)) {
                        synchronized (mDownloadList) {
                            mDownloadList.remove(downLoadTask);
                        }
                    }
                }
            };
        }
        if (task != null && mDownloadList != null && !mDownloadList.contains(task)) {
            task.setCancelListen(mDownloadCancel);
            synchronized (mDownloadList) {
                mDownloadList.add(task);
            }
        }
    }

    /**
     * 保存任务
     *
     * @param downloadInfo
     */
    private static void saveTask(DownloadInfo downloadInfo) {
        if (downloadInfo != null && downloadInfo.isExists()) {
            TaskModel model = new TaskModel();
            model.setId(downloadInfo.getId());
            model.setUrl(downloadInfo.getDownloadUrl());
            model.setPath(downloadInfo.getLocalPath());
            model.setStartTime(new Date().getTime());
            TaskHolder.getInstance().add(model);
        }
    }

    /**
     * 根据downLoadId获取任务
     *
     * @param downloadId
     * @return
     */
    private static DownLoadTask getTaskByDownloadId(long downloadId, String downloadUrl) {
        synchronized (mDownloadList) {
            DownLoadTask task;
            for (int i = 0; i < getTasks().size(); i++) {
                task = getTasks().get(i);
                if (task.getDownLoadInfo().getId() == downloadId) {
                    return task;
                } else if (!TextUtils.isEmpty(downloadUrl) && downloadUrl.equals(task.getDownLoadInfo().getDownloadUrl())) {
                    return task;
                }
            }
        }
        return null;
    }

    /**
     * 根据downloadId获取下载任务
     *
     * @param downloadId the download id
     * @return the task
     */
    public static DownLoadTask getTask(long downloadId) {
        if (downloadId >= 0) {
            DownLoadTask task = getTaskByDownloadId(downloadId, null);
            if (task != null) return task;
            TaskModel model = TaskHolder.getInstance().getModel(downloadId);
            if (model != null) {
                task = new DownLoadTask(getContext(), getManager(), model.getId());
            } else {
                task = new DownLoadTask(getContext(), getManager(), downloadId);
            }
            addTaskToList(task);
            return task;

        }
        return null;
    }


    /**
     * 根据url获取下载任务.
     *
     * @param downloadUrl the download url
     * @return the task
     */
    public static DownLoadTask getTask(String downloadUrl) {
        if (!TextUtils.isEmpty(downloadUrl)) {
            DownLoadTask task = getTaskByDownloadId(Integer.MIN_VALUE, downloadUrl);
            if (task != null) return task;
            TaskModel model = TaskHolder.getInstance().getModel(downloadUrl);
            if (model != null) {
                task = new DownLoadTask(getContext(), getManager(), model.getId());
                addTaskToList(task);
                return task;
            }
        }
        return null;
    }


}
