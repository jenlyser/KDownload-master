package com.wking.download;

public interface IDownLoadTask {
    /**
     * 添加下载监听
     *
     * @param listener 监听接口
     */
    void registerListener(IDownLoadListener listener);

    /**
     * 取消下载监听
     *
     * @param listener
     */
    void unregisterListener(IDownLoadListener listener);

    /**
     * 获取下载数据
     *
     * @return
     */
    DownloadInfo getDownLoadInfo();

    /**
     * 暂停下载
     *
     * @return
     */
    boolean pause();

    /**
     * 恢复下载
     *
     * @return
     */
    boolean resume();

    /**
     * 取消下载
     *
     * @return
     */
    boolean cancel();

}
