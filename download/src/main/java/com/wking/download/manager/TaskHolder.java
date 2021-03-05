package com.wking.download.manager;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

/**
 * 下载任务数据,负责保存,修改,加载下载任务
 *
 * @Author Sean
 * @Date 2021 /3/3
 * @Description TaskHolder.java
 */
public class TaskHolder {
    private String TAG = "TaskHolder";
    private TaskData mData = null;
    private Object mLock = new Object();
    private Object mDataLock = new Object();
    private String mDataFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/data/.download_wk.log";
    private File mDataFile;

    //single instance object
    private static TaskHolder mTaskHolder;


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static TaskHolder getInstance() {
        if (mTaskHolder == null) {
            mTaskHolder = new TaskHolder();
        }
        return mTaskHolder;
    }

    /**
     * Instantiates a new Task holder.
     */
    public TaskHolder() {
        init();
    }

    /**
     * Add boolean.
     *
     * @param model the model
     * @return the boolean
     */
    public boolean add(TaskModel model) {
        synchronized (mDataLock) {
            mData.getTaskData().add(0, model);
        }
        mData.setLastModify(new Date().getTime());
        return saveData(mData);
    }

    /**
     * Gets model.
     *
     * @param id the id
     * @return the model
     */
    public TaskModel getModel(long id) {
        TaskModel item;
        synchronized (mDataLock) {
            for (int i = 0; i < mData.getTaskData().size(); i++) {
                item = mData.getTaskData().get(i);
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Gets model.
     *
     * @param url the url
     * @return the model
     */
    public TaskModel getModel(String url) {
        if (!TextUtils.isEmpty(url)) {
            TaskModel item;
            synchronized (mDataLock) {
                for (int i = 0; i < mData.getTaskData().size(); i++) {
                    item = mData.getTaskData().get(i);
                    if (url.equals(item.getUrl())) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Remove boolean.
     *
     * @param model the model
     * @return the boolean
     */
    public boolean remove(TaskModel model) {
        if (model == null || !mData.getTaskData().contains(model)) return true;
        synchronized (mDataLock) {
            mData.getTaskData().remove(model);
            return saveData(mData);
        }
    }

    /**
     * Remove boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean remove(long id) {
        if (mData == null || mData.getTaskData() == null || mData.getTaskData().size() < 1)
            return true;
        TaskModel itemDel = getModel(id);
        return remove(itemDel);
    }

    /**
     * Remove boolean.
     *
     * @param url the url
     * @return the boolean
     */
    public boolean remove(String url) {
        TaskModel itemDel = getModel(url);
        return remove(itemDel);
    }


    /**
     * 初始化数据
     */
    public void init() {
        //数据为空先从磁盘加载数据
        if (mData == null) {
            mData = loadData();
        }
        //数据为空新建数据
        if (mData == null) {
            mData = new TaskData();
        }
    }

    /**
     * 获取数据
     *
     * @return
     */
    public TaskData getData() {
        if (mData == null) {
            init();
        }
        return mData;
    }

    /**
     * 获取数据列表
     *
     * @return
     */
    public List<TaskModel> getDataList() {
        if (mData == null) {
            init();
        }
        return mData.getTaskData();
    }

    /**
     * 从磁盘中加载数据
     *
     * @return
     */
    private TaskData loadData() {
        if (mDataFile == null) {
            mDataFile = new File(mDataFileName);
        }
        if (mDataFile == null || !mDataFile.exists()) {
            return null;
        }
        Log.d(TAG, "data load path:" + mDataFileName);
        TaskData data = null;
        ObjectInputStream inputStream = null;
        synchronized (mLock) {
            try {
                inputStream = new ObjectInputStream(new FileInputStream(mDataFile));
                data = (TaskData) inputStream.readObject();
            } catch (Exception e) {
                Log.e(TAG, "Load failure;" + e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
        return data;
    }

    /**
     * 保存数据到磁盘
     *
     * @param data
     * @return
     */
    private boolean saveData(TaskData data) {
        synchronized (mLock) {
            if (mDataFile == null) {
                mDataFile = new File(mDataFileName);
            }
            if (!mDataFile.getParentFile().exists()) {
                mDataFile.getParentFile().mkdirs();
            }
            Log.d(TAG, "data save path:" + mDataFileName);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(mDataFile));
                outputStream.writeObject(data);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "write error: " + e.getMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
        return false;
    }
}
