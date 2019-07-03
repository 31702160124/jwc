package com.example.a18433.jwcmmvtc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.Service.cookieService;
import com.example.a18433.jwcmmvtc.config.Constant;

import java.io.IOException;
import java.util.Map;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getError;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getUserConfig;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveUserConfig;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setLoginTime;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setname;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText user, pwd, code;
    private Button login;
    private String codestr, usestr, pwdstr;
    private ImageView code_image;
    private TextView Tv_err;
    private RelativeLayout login_tv;
    private Switch show_pwd;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @SuppressLint("NewApi")
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
            window.setStatusBarColor(Color.TRANSPARENT);//设置状态栏颜色和主布局背景颜色相同
//            window.setStatusBarColor(Color.parseColor("#45b97f"));//设置状态栏为指定颜色
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initEdit();
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, final boolean hasFocus) {
                Log.i("onFocusChange", "onFocusChange: " + view.getId() + hasFocus);
                if (hasFocus) {
                    InputMethodManager manager = ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        postLogin();
                    }
                }
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    protected void onStart() {
        super.onStart();
        if (!getError().equals("用户点击退出")) {
            Tv_err.setText(getError());
            Tv_err.setBackground(getResources().getDrawable(R.drawable.radio2));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        MainActivity.MainACTIVITYFlg = false;
        login_tv = (RelativeLayout) findViewById(R.id.login_tv);
        show_pwd = (Switch) findViewById(R.id.Login_show_pwd);
        show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                if (b) {
                    setHide_pwd();
                } else {
                    setShow_pwd();
                }
            }
        });
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                login_tv.setBackground(Constant.getRandm(Constant.loginarray));
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 100);
        Tv_err = (TextView) findViewById(R.id.Tv_err);
        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);
        code = (EditText) findViewById(R.id.code);
        code_image = (ImageView) findViewById(R.id.code_image);
        codestr = code.getText().toString().trim();
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        code_image.setOnClickListener(this);
        showCheckImg();
    }

    private void goToMain() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initEdit() {
        Map map = getUserConfig();
        user.setText((String) map.get("username"));
        pwd.setText((String) map.get("password"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                postLogin();
                break;
            case R.id.code_image:
                showCheckImg();
                break;
        }
    }

    private void showCheckImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap img = cookieService.getJwcdao().getCheckCodeImg();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            code_image.setImageBitmap(img);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postLogin() {
        usestr = user.getText().toString().trim();
        pwdstr = pwd.getText().toString().trim();
        codestr = code.getText().toString().trim();
        // 判断参数是否为空
        if (usestr.equals("")) {
            showAlertDialog("请输入用户名");
//            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwdstr.equals("")) {
            showAlertDialog("请输入密码");
//            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (codestr.equals("")) {
            showAlertDialog("请输入验证码");
//            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("登录中");
        progressDialog.setMessage("Loading");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] result = cookieService.getJwcdao().Login(usestr, pwdstr, codestr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
                switch (result[0]) {
                    case "checkCode_error":
                    case "pwd_error":
                    case "user_error":
                    case "user_locked":
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                code.setText("");
                            }
                        });
                        showCheckImg();
                        showHintsMsg(result[1]);
                        break;
                    case "ok":
                        MainActivity.MainACTIVITYFlg = true;
                        saveUserConfig(usestr, pwdstr);
                        setname(result[1]);
                        setLoginTime();
                        showHintsMsg(result[1]);
                        break;
                    default:
                        showCheckImg();
                        showHintsMsg("程序出错，登录失败");
                        break;
                }
            }
        }).start();
    }

    private void showHintsMsg(String s) {
        Looper.prepare();
        showAlertDialog(s);
        Looper.loop();
    }

    private void showAlertDialog(final String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(string);     //设置对话框标题

        //        builder.setIcon(R.drawable.ic_launcher);      //设置对话框标题前的图标

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (string.contains("欢迎")) {
                    goToMain();
                }
            }
        });

        builder.setCancelable(true);   //设置按钮是否可以按返回键取消,false则不可以取消

        AlertDialog dialog = builder.create();  //创建对话框

        dialog.setCanceledOnTouchOutside(false);      //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏

        dialog.show();
    }

    private synchronized void setShow_pwd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HideReturnsTransformationMethod show = HideReturnsTransformationMethod.getInstance();
                pwd.setTransformationMethod(show);
            }
        });
    }

    private synchronized void setHide_pwd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PasswordTransformationMethod hide = PasswordTransformationMethod.getInstance();
                pwd.setTransformationMethod(hide);
            }
        });
    }

}


