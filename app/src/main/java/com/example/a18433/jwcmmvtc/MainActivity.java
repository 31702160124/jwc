package com.example.a18433.jwcmmvtc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.Service.cookieService;
import com.example.a18433.jwcmmvtc.config.Constant;
import com.example.a18433.jwcmmvtc.fragment.LeftFragment;
import com.example.a18433.jwcmmvtc.fragment.RightFragment;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.other_fragment;
import com.example.a18433.jwcmmvtc.fragment.workFragment;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.delError;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.isFristlogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveIslogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saversFristlogn;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.userIsLogin;


public class MainActivity extends FragmentActivity implements RightFragment.showPane,
        LeftFragment.closePane {
    public SlidingPaneLayout slp;
    public volatile static Boolean MainACTIVITYFlg = true;
    public static Thread thread;
    private RightFragment rightFragment;
    private LeftFragment leftFragment;

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
        slp.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(@NonNull View panel, float slideOffset) {

            }

            @Override
            public void onPanelOpened(@NonNull View panel) {

            }

            @Override
            public void onPanelClosed(@NonNull View panel) {
                getSupportFragmentManager()
                        .findFragmentById(R.id.left_fragment)
                        .getView()
                        .findViewById(R.id.img_Left_tv).setBackground(Constant.getRandm(Constant.bgarray));
            }
        });
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
    }

    private void showANDelouse() throws Exception {
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
        delError();
        saveIslogin(false);
        MainACTIVITYFlg = false;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void rightStates(String content, int id) {
        slp.closePane();
        if (getCookie().isEmpty()) {
            loginOut();
        } else {
            rightFragment.setTite(content);
            rightFragment.addFragments(id);
        }
    }

    private void studentLoginTrue() {
        MainACTIVITYFlg = true;
        rightFragment.addFragments(4);
        saversFristlogn(false);
        Toast.makeText(this, "教务管理系统欢迎你", Toast.LENGTH_SHORT).show();
        workFragment workFragment = (workFragment) getSupportFragmentManager().findFragmentByTag("work");
        if (workFragment == null) {
            workFragment = new workFragment();
            getSupportFragmentManager().beginTransaction().add(workFragment, "work").commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}