package com.example.a18433.jwcmmvtc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.Service.cookieService;

import java.io.IOException;
import java.util.Map;

import static com.example.a18433.jwcmmvtc.MyApplication.getContext;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getError;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getUserConfig;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveUserConfig;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setname;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText user, pwd, code;
    private Button login;
    private String codestr, usestr, pwdstr;
    private ImageView code_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initEdit();
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.i("onFocusChange", "onFocusChange: " + view.getId() + hasFocus);
                if (hasFocus) {
                    InputMethodManager manager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        postLogin();
                    }
                }
            }
        });
    }

    private void init() {
        MainActivity.MainACTIVITYFlg = false;
        if (!getError().equals("用户点击退出")) {
            Toast.makeText(this, getError(), Toast.LENGTH_SHORT).show();
        }
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);
        code = findViewById(R.id.code);
        code_image = findViewById(R.id.code_image);
        codestr = code.getText().toString().trim();
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        code_image.setOnClickListener(this);
        showCheckImg();
    }

    private void goToMain() throws Exception {
        Intent intent = new Intent(getContext(), MainActivity.class);
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
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwdstr.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (codestr.equals("")) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
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
                        code.setText("");
                    }
                });
                switch (result[0]) {
                    case "checkCode_error":
                    case "pwd_error":
                    case "user_error":
                    case "user_locked":
                        showCheckImg();
                        showHintsMsg(result[1]);
                        break;
                    case "ok":
                        MainActivity.MainACTIVITYFlg = true;
                        saveUserConfig(usestr, pwdstr);
                        setname(result[1]);
                        try {
                            goToMain();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

}


