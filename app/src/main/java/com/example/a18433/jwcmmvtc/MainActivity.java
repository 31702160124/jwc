package com.example.a18433.jwcmmvtc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.Service.cookieService;
import com.example.a18433.jwcmmvtc.fragment.LeftFragment;
import com.example.a18433.jwcmmvtc.fragment.RightFragment;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.addEerror;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.isFristlogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveIslogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveisFristlogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.userIsLogin;


public class MainActivity extends AppCompatActivity implements RightFragment.showPane, LeftFragment.closePane {
    public SlidingPaneLayout slp;
    public volatile static Boolean MainACTIVITYFlg;

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
        if (isFristlogin()) {
            showANDclose();
        }
        if (!userIsLogin()) {
            MainACTIVITYFlg = false;
            loginOut();
        } else {
            MainACTIVITYFlg = true;
            saveisFristlogin(false);
            Toast.makeText(MyApplication.getContext(), "教务管理系统欢迎你", Toast.LENGTH_SHORT).show();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainACTIVITYFlg) {
                    if (getCookie().isEmpty()) {
                        MainACTIVITYFlg = true;
                        loginOut();
                        addEerror();
                    }else {
                        saveIslogin(true);
                    }
                }
            }
        }).start();

    }

    private void showANDclose()throws Exception {
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
            RightFragment rightFragment = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.right_fragment);
            rightFragment.setTite(content);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainACTIVITYFlg = false;
    }
}
