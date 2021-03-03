package com.wking.download.demo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Object mLock = new Object();
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(() -> {
            downloadTest();
        }).start();
    }

    private String downloadUrl = "https://dw.fjweite.cn/syt/windows_10_professional_x64_2020.iso";

    private void downloadTest() {
        Uri uri = Uri.parse(downloadUrl);
        Log.d(TAG, uri.toString());

    }

}