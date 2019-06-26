package com.example.a18433.jwcmmvtc.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.a18433.jwcmmvtc.utils.jwcDao;

import java.util.Date;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getLoginTime;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setLoginTime;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.userIsLogin;

public class cookieService extends Service {
    private static volatile Boolean isLoginFlg = true;
    private static volatile Boolean isOverFlg = true;
    private static volatile Boolean flg = true;
    private String TAG = "cookieService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*
     * 服务执行
     *
     * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand is Run----------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() throws Exception {
        while (flg) {
            int i = 1;
            while (isLoginFlg) {
                Thread.sleep(5000);
                if (!userIsLogin()) {
                    setLoginTime();
                    Log.i(TAG, new Date().getTime() - getLoginTime() + "用户未登录 isLoginFlg " + getLoginTime());
                    isOverFlg = false;
                    isLoginFlg = true;
                } else {
                    Log.i(TAG, new Date().getTime() - getLoginTime() + "用户cokie isLoginFlg " + getLoginTime());
                    Log.i(TAG, "用户已登录 isLoginFlg " + i++ + getCookie());
                    isLoginFlg = false;
                    isOverFlg = true;
                }
            }
            //  1,800,000  半小时
            while (isOverFlg) {
                if (!getCookie().isEmpty()) {
                    isLoginFlg = false;
                    isOverFlg = true;
                    if (new Date().getTime() - getLoginTime() > 5000) {
                        getJwcdao().cookieIsOverdue();
                        setLoginTime();
                    }
                } else {
                    Log.i(TAG, "cookie失效 isLoginFlg " + i++ + getCookie());
                    isOverFlg = false;
                    isLoginFlg = true;
                }
            }
        }
    }

    public static jwcDao getJwcdao() {
        return jwcDao.getInstance();
    }
}

