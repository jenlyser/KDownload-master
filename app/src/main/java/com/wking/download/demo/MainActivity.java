package com.wking.download.demo;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.wking.download.DownloadTask;
import com.wking.download.DownloadInfo;
import com.wking.download.DownloadManagerPro;
import com.wking.download.IDownloadListener;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Object mLock = new Object();
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_pause_download).setOnClickListener(this::onClick);
        findViewById(R.id.btn_resume_download).setOnClickListener(this::onClick);
        findViewById(R.id.btn_start_download).setOnClickListener(this::onClick);
        findViewById(R.id.btn_cancel_download).setOnClickListener(this::onClick);
        findViewById(R.id.btn_progress_download).setOnClickListener(this::onClick);
    }


    private String downloadUrl = "https://dw.fjweite.cn/syt/windows_10_professional_x64_2020.iso";
    private DownloadTask mDownTask;
    private boolean executeStatus;

    private void downloadTest(String dUrl, String fileName) {
        mDownTask = DownloadManagerPro.getTask(dUrl);
        if (mDownTask != null) {
            Log.d(TAG, mDownTask.getDownLoadInfo().getProgress() + "%");
            return;
        }
        Uri uri = Uri.parse(dUrl);
        Log.d(TAG, uri.toString());
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dUrl));
        request.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setMimeType("application/x-www-form-urlencoded");

        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(true);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载文件");
        request.setDescription("下载描述");
        //        request.setVisibleInDownloadsUi(true);
        //设置下载的路径
        File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        request.setDestinationUri(Uri.fromFile(file));

        mDownTask = DownloadManagerPro.startDownload(request);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_download:
//                downloadTest(downloadUrl, "111.iso");
                downloadUrl = "https://ip4062117236.mobgslb.tbcache.com/fs01/union_pack/Wandoujia_3266791_web_seo_baidu_homepage.apk?ali_redirect_domain=alissl.ucdl.pp.uc.cn&ali_redirect_ex_ftag=0f09e940439d5c73db899050d0dd1b82f143305010517c12&ali_redirect_ex_tmining_ts=1614915009&ali_redirect_ex_tmining_expire=3600&ali_redirect_ex_hot=100";
                downloadTest(downloadUrl, "test.apk");
                break;
            case R.id.btn_progress_download:
                if (mDownTask != null) {
                    mDownTask.registerListener(new IDownloadListener() {
                        @Override
                        public void onPause() {
                            Log.d(TAG, "onPause");

                        }

                        @Override
                        public void onResume() {
                            Log.d(TAG, "onResume");

                        }

                        @Override
                        public void onProgress(double percent, DownloadInfo info) {

                            Log.d(TAG, "onProgress:" + percent);
                        }

                        @Override
                        public void onComplete(boolean status, DownloadInfo info) {
                            Log.d(TAG, "onComplete:" + status);
                        }
                    });
                }
                break;
            case R.id.btn_pause_download:
                if (mDownTask != null) {
                    executeStatus = mDownTask.pause();
                    Log.d(TAG, "PAUSE:" + executeStatus);
                }
                break;

            case R.id.btn_resume_download:
                if (mDownTask != null) {
                    executeStatus = mDownTask.resume();
                    Log.d(TAG, "Resume:" + executeStatus);
                }
                break;
            case R.id.btn_cancel_download:
                if (mDownTask != null) {
                    executeStatus = mDownTask.cancel();
                    Log.d(TAG, "Cancel:" + executeStatus);
                }
                break;

        }
    }



}