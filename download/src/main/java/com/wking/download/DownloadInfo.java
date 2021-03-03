package com.wking.download;

import android.app.DownloadManager;
import android.net.Uri;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DownloadManager的下载信息
 *
 * @Author Sean
 * @Date 2021 /3/3
 * @Description DownloadInfo.java
 */
public class DownloadInfo {
    private long id;//DownloadManager任务ID
    private String title;//下载标题
    private String description;//下载描述
    private int downloadSizeBytes = 0;//DownloadManager已经下载的数据大小DOWNLOADED_SO_FAR
    private int totalSizeBytes = 0;//DownloadManager下载文件的总大小
    private long lastModifiedTimestamp = 0;//最后修改的时间
    private Uri uri;//下载的地址
    private Uri localUri;//本地文件的路径
    private Uri mediaProviderUri;//
    private String mediaType;//媒体类型
    private int reason;//错误原由
    private int status = Integer.MIN_VALUE;//下载状态

    /**
     * Instantiates a new Download info.
     */
    public DownloadInfo() {
    }

    //region  sets value

    /**
     * Sets id.
     *
     * @param id the id
     */
    void setId(long id) {
        this.id = id;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets download size bytes.
     *
     * @param downloadSizeBytes the download size bytes
     */
    void setDownloadSizeBytes(int downloadSizeBytes) {
        this.downloadSizeBytes = downloadSizeBytes;
    }

    /**
     * Sets total size bytes.
     *
     * @param totalSizeBytes the total size bytes
     */
    void setTotalSizeBytes(int totalSizeBytes) {
        this.totalSizeBytes = totalSizeBytes;
    }

    /**
     * Sets last modified timestamp.
     *
     * @param lastModifiedTimestamp the last modified timestamp
     */
    void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    /**
     * Sets uri.
     *
     * @param uri the uri
     */
    void setUri(Uri uri) {
        this.uri = uri;
    }

    /**
     * Sets local uri.
     *
     * @param localUri the local uri
     */
    void setLocalUri(Uri localUri) {
        this.localUri = localUri;
    }

    /**
     * Sets media provider uri.
     *
     * @param mediaProviderUri the media provider uri
     */
    void setMediaProviderUri(Uri mediaProviderUri) {
        this.mediaProviderUri = mediaProviderUri;
    }

    /**
     * Sets media type.
     *
     * @param mediaType the media type
     */
    void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * Sets reason.
     *
     * @param reason the reason
     */
    void setReason(int reason) {
        this.reason = reason;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    void setStatus(int status) {
        this.status = status;
    }

    //endregion

    //region gets value

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets download size bytes.
     *
     * @return the download size bytes
     */
    public int getDownloadSizeBytes() {
        return downloadSizeBytes;
    }

    /**
     * Gets total size bytes.
     *
     * @return the total size bytes
     */
    public int getTotalSizeBytes() {
        return totalSizeBytes;
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public double getProgress() {
        double percent = downloadSizeBytes / (totalSizeBytes * 1.0);
        return new BigDecimal(percent * 100)
                .setScale(2, BigDecimal.ROUND_UNNECESSARY)
                .doubleValue();
    }

    /**
     * Gets last modified time.
     *
     * @return the last modified time
     */
    public Date getLastModifiedTime() {
        return new Date(lastModifiedTimestamp);
    }


    /**
     * Gets last modified timestamp.
     *
     * @return the last modified timestamp
     */
    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    /**
     * Gets uri.
     *
     * @return the uri
     */
    public Uri getUri() {
        return uri;
    }

    /**
     * Gets local uri.
     *
     * @return the local uri
     */
    public Uri getLocalUri() {
        return localUri;
    }

    /**
     * Gets media provider uri.
     *
     * @return the media provider uri
     */
    public Uri getMediaProviderUri() {
        return mediaProviderUri;
    }

    /**
     * Gets media type.
     *
     * @return the media type
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Gets reason.
     *
     * @return the reason
     */
    public int getReason() {
        return reason;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    //endregion

    /**
     * Is complete boolean.
     *
     * @return the boolean
     */
    public boolean isComplete() {
        return DownloadManager.STATUS_SUCCESSFUL == status || DownloadManager.STATUS_FAILED == status;
    }

    /**
     * Is successful boolean.
     *
     * @return the boolean
     */
    public boolean isSuccessful() {
        return DownloadManager.STATUS_SUCCESSFUL == status;
    }

    /**
     * Is failed boolean.
     *
     * @return the boolean
     */
    public boolean isFailed() {
        return DownloadManager.STATUS_FAILED == status;
    }

    /**
     * Is running boolean.
     *
     * @return the boolean
     */
    public boolean isRunning() {
        return DownloadManager.STATUS_RUNNING == status;
    }

    /**
     * Is paused boolean.
     *
     * @return the boolean
     */
    public boolean isPaused() {
        return DownloadManager.STATUS_PAUSED == status;
    }

    /**
     * Is pending boolean.
     *
     * @return the boolean
     */
    public boolean isPending() {
        return DownloadManager.STATUS_PENDING == status;
    }

    /**
     * Is exists boolean.
     *
     * @return the boolean
     */
    public boolean isExists() {
        return Integer.MIN_VALUE != status;
    }

}
