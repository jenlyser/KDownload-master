package com.wking.download;

/**
 * 下载监听类
 *
 * @Author Sean
 * @Date 2021 /3/3
 * @Description IDownLoadListener.java
 */
public interface IDownloadListener {

    /**
     * On pause.
     */
    void onPause();

    /**
     * On resume.
     */
    void onResume();

    /**
     * On progress.
     *
     * @param percent the percent
     * @param info    the info
     */
    void onProgress(double percent, DownloadInfo info);

    /**
     * On complete.
     *
     * @param status the status
     * @param info   the info
     */
    void onComplete(boolean status, DownloadInfo info);


}
