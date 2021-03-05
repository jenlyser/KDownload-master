package com.wking.download.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    private boolean toNext = false;// 是否已经跳转过窗体
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getPermissions();
    }


    protected void startLoad() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                StartActivity.this.Adjust();
            }
        }, 1500L);
    }

    public void Adjust() {
        if (toNext == true) {// 有一个延时的hander 和点击都可以触发跳转,为了避免重复触发跳转窗体事件.
            return;
        }
        toNext = true;
        boolean isToAdActivity = false;

        //直接进入主页
        toNextActivity(MainActivity.class);
//        toNextActivity(TestMainActivity.class);
    }


    /**
     * 进入下一个窗体
     *
     * @param tClass
     */
    public void toNextActivity(Class<?> tClass) {
        Intent localIntent = new Intent();
        localIntent.setClass(this, tClass);
        startActivity(localIntent);
        finish();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static int REQUEST_CODE = 1;

    /**
     * 获取读写权限
     */
    private void getPermissions() {
        boolean isGetAllPermission = true;
        //判断当前版本
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (String item : PERMISSIONS_STORAGE) {
                if (ActivityCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                    isGetAllPermission = false;
                    break;
                } else {
                    Log.d(TAG, item + ":已有权限");
                }
            }
        } else {
            Log.d(TAG, "小于 6.0,不需要获取权限");
        }
        if (isGetAllPermission) {
            startLoad();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_CODE);//请求权限
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == 0) {
                    Log.d(TAG, grantResults[i] + ":获取权限成功");
//                    perSucceed();
                } else {
                    Log.d(TAG, grantResults[i] + ":获取权限失败");
//                    ToastUtils.showToast(mContext, "权限被拒绝,连接USB设备终止");
                }
            }
            //其实需要判断部分重要权限是否获取,否则还是会出现问题.
            startLoad();
        }
    }
}