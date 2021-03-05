package com.wking.download.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 下载任务列表,保存在文件中的数据
 *
 * @Author Sean
 * @Date 2021 /3/3
 * @Description TaskData.java
 */
public class TaskData implements Serializable {
    private long lastModify = new Date().getTime();
    private List<TaskModel> taskData = new ArrayList<>();

    /**
     * Instantiates a new Task list.
     */
    public TaskData() {
    }

    /**
     * Instantiates a new Task list.
     *
     * @param lastModify the last modify
     * @param taskData   the task data
     */
    public TaskData(long lastModify, List<TaskModel> taskData) {
        this.lastModify = lastModify;
        this.taskData = taskData;
    }

    /**
     * Sets last modify.
     *
     * @param lastModify the last modify
     */
    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    /**
     * Gets last modify.
     *
     * @return the last modify
     */
    public long getLastModify() {
        return lastModify;
    }

    /**
     * Gets last modify date.
     *
     * @return the last modify date
     */
    public Date getLastModifyDate() {
        return new Date(lastModify);
    }

    /**
     * Gets task data.
     *
     * @return the task data
     */
    public List<TaskModel> getTaskData() {
        return taskData;
    }
}
