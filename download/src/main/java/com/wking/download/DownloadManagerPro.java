package com.wking.download;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;

import com.wking.download.manager.TaskHolder;
import com.wking.download.manager.TaskModel;

import java.util.ArrayList;
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
    private static List<DownLoadTask> mDownloadList;

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
                    addTaskToList(new DownLoadTask(mContext, mManager, taskModels.get(i).getId()));
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
        if (task != null && !getTasks().contains(task)) {
            synchronized (mDownloadList) {
                getTasks().add(task);
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
            model.setUrl(downloadInfo.getUri().getPath());
            model.setPath(downloadInfo.getLocalUri().getPath());
            TaskHolder.getInstance().add(model);
        }
    }

    /**
     * 根据downloadId获取下载任务
     *
     * @param downloadId the download id
     * @return the task
     */
    public static DownLoadTask getTask(long downloadId) {
        if (downloadId >= 0) {
            synchronized (mDownloadList) {
                DownLoadTask task;
                for (int i = 0; i < getTasks().size(); i++) {
                    task = getTasks().get(i);
                    if (task.getDownLoadInfo().getId() == downloadId) {
                        return task;
                    }
                }
                TaskModel model = TaskHolder.getInstance().getModel(downloadId);
                if (model != null) {
                    task = new DownLoadTask(getContext(), getManager(), model.getId());
                } else {
                    task = new DownLoadTask(getContext(), getManager(), downloadId);
                }
                addTaskToList(task);
                return task;
            }
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
            synchronized (mDownloadList) {
                DownLoadTask task;
                for (int i = 0; i < getTasks().size(); i++) {
                    task = getTasks().get(i);
                    if (downloadUrl.equals(task.getDownLoadInfo().getUri().toString())) {
                        return task;
                    }
                }
                TaskModel model = TaskHolder.getInstance().getModel(downloadUrl);
                if (model != null) {
                    task = new DownLoadTask(getContext(), getManager(), model.getId());
                    addTaskToList(task);
                    return task;
                }
            }
        }
        return null;
    }


}
