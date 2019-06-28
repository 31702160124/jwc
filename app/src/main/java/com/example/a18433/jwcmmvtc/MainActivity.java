package com.example.a18433.jwcmmvtc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.Service.cookieService;
import com.example.a18433.jwcmmvtc.entity.user;
import com.example.a18433.jwcmmvtc.fragment.LeftFragment;
import com.example.a18433.jwcmmvtc.fragment.RightFragment;

import java.util.ArrayList;
import java.util.Map;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.isFristlogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveIslogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saversFristlogn;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.userIsLogin;


public class MainActivity extends FragmentActivity implements RightFragment.showPane,
        LeftFragment.closePane, SlidingPaneLayout.PanelSlideListener {
    public SlidingPaneLayout slp;
    public volatile static Boolean MainACTIVITYFlg = true;
    private static Bitmap bitmap;
    private static Map<String, String> map;
    private static ArrayList<user> dataList;
    public static Thread thread;
    private Handler handler;
    private RightFragment rightFragment;
    private LeftFragment leftFragment;
    private static String TAG = "主activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //沉浸式状态栏
        //4.4以上设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 5.0以上系统状态栏透明，
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);//设置状态栏颜色和主布局背景颜色相同
            window.setStatusBarColor(Color.parseColor("#03A9F4"));//设置状态栏为指定颜色
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        slp = findViewById(R.id.slp);
        slp.setPanelSlideListener(this);
        leftFragment = (LeftFragment) getSupportFragmentManager().findFragmentById(R.id.left_fragment);
        rightFragment = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.right_fragment);
        if (isFristlogin()) {
            showANDelouse();
        }
        if (!userIsLogin()) {
            loginOut();
        } else {
            studentLoginTrue();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainACTIVITYFlg) {
                    if (getCookie().isEmpty()) {
                        loginOut();
                    }
                }
            }
        }).start();
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    bitmap = cookieService.getJwcdao().getHeadPic();
                    setBitmap(bitmap);
                    dataList = cookieService.getJwcdao().getZGrade();
                    setDataList(dataList);
                    map = cookieService.getJwcdao().getPersonalInfo();
                    setMap(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        handler.post(thread);
    }

    private void showANDelouse() throws Exception {
        Thread.sleep(1000);
        slp.openPane();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                slp.closePane();
            }
        }, 1000);
    }

    @Override
    public void showPane() {
        slp.openPane();
    }

    @Override
    public void loginOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cookieService.getJwcdao().jwcloginOut();
            }
        }).start();
        saveIslogin(false);
        MainACTIVITYFlg = false;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void closePane(String content) {
        slp.closePane();
        if (getCookie().isEmpty()) {
            loginOut();
        } else {
            rightFragment.setTite(content);
        }
    }

    @Override
    public void rightFaStates(int id) {
        rightFragment.addFragments(id);
    }

    private void studentLoginTrue() {
        MainACTIVITYFlg = true;
        saversFristlogn(false);
        Toast.makeText(MyApplication.getContext(), "教务管理系统欢迎你", Toast.LENGTH_SHORT).show();
    }


    public static void setBitmap(Bitmap bitmap) {
        MainActivity.bitmap = bitmap;
    }

    public static void setMap(Map<String, String> map) {
        MainActivity.map = map;
    }

    public static void setDataList(ArrayList<user> dataList) {
        MainActivity.dataList = dataList;
    }

    public static Map<String, String> getMap() {
        return map;
    }

    public static ArrayList<user> getDataList() {
        return dataList;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onPanelSlide(@NonNull View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(@NonNull View panel) {
    }

    @Override
    public void onPanelClosed(@NonNull View panel) {

    }

}