package com.wking.download;

public interface IDownloadTask {
    /**
     * 添加下载监听
     *
     * @param listener 监听接口
     */
    void registerListener(IDownloadListener listener);

    /**
     * 取消下载监听
     *
     * @param listener
     */
    void unregisterListener(IDownloadListener listener);

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
