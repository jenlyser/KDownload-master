package com.wking.download;

/**
 * DownloadManager 取消接口
 * @Author Sean
 * @Date 2021/3/5
 * @Description IDownloadCancel.java
 */
interface IDownloadCancel {
    /**
     * 取消时进行回调
     * @param id
     */
    void onCancel(long id);
}
