package com.example.httpdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.httpdemo.Utils.Tools;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private ImageView iv_code;
    private EditText et_user, et_pwd, et_code;
    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        tools = Tools.getInstance();
        showCheckImg();
    }

    private void init() {
        et_user = findViewById(R.id.et_user);
        et_pwd = findViewById(R.id.et_pwd);
        et_code = findViewById(R.id.et_check);
        btnLogin = findViewById(R.id.btn_login);
        iv_code = findViewById(R.id.iv_ckcode);

        btnLogin.setOnClickListener(this);
        iv_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ckcode:
                showCheckImg();
                break;
            case R.id.btn_login:
                postLogin();
                break;
        }
    }

    private void showCheckImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] data = tools.getCheckCodeImg();
                    final Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_code.setImageBitmap(img);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void postLogin() {
        final String username = et_user.getText().toString().trim();
        final String password = et_pwd.getText().toString().trim();
        final String checkCode = et_code.getText().toString().trim();
        // 判断参数是否为空
        if (username.equals("")) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkCode.equals("")) {
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
                String[] result = tools.Login(username, password, checkCode);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        et_code.setText("");
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
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("info", result[1]);
                        startActivity(intent);
                        finish();
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
