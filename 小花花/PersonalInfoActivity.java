package com.example.httpdemo;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.httpdemo.Utils.Tools;

import java.io.IOException;
import java.util.Map;

public class PersonalInfoActivity extends AppCompatActivity {
    private Tools tools;
    private TextView tv_xh,tv_xm,tv_sex,tv_zzmm,tv_xi,tv_zymc,tv_xzb,tv_dqszj,tv_rxrq,tv_xlcc,tv_xxxs,tv_xz,tv_xjzt;
    private ImageView img;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        tools = Tools.getInstance();
        init();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Map<String,String> map = tools.getPersonalInfo();
                if (!map.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_xh.setText("学号   "+ map.get("xh"));
                            tv_xm.setText("姓名   "+ map.get("xm"));
                            tv_sex.setText("性别   "+ map.get("sex"));
                            tv_zzmm.setText("政治面貌   "+ map.get("zzmm"));
                            tv_xi.setText("系   "+ map.get("xi"));
                            tv_zymc.setText("专业名称   "+ map.get("zymc"));
                            tv_xzb.setText("行政班   "+ map.get("xzb"));
                            tv_dqszj.setText("当前所在级   "+ map.get("dqszj"));
                            tv_rxrq.setText("入学日期   "+ map.get("rxrq"));
                            tv_xlcc.setText("学历层次   "+ map.get("xlcc"));
                            tv_xxxs.setText("学习形式   "+ map.get("xxxs"));
                            tv_xz.setText("学制   "+ map.get("xz"));
                            tv_xjzt.setText("学籍状态   "+ map.get("xjzt"));
                        }
                    });

                    // 学生照片
                    try {
                        byte[] data = tools.getHeadPic();
                        final Bitmap photo = BitmapFactory.decodeByteArray(data, 0, data.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageBitmap(photo);
                                progressDialog.hide();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                        }
                    });
                    Toast.makeText(PersonalInfoActivity.this, "获取个人信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    private void init() {
        tv_xh = findViewById(R.id.tv_xh);
        tv_xm = findViewById(R.id.tv_xm);
        tv_sex = findViewById(R.id.tv_sex);
        tv_zzmm = findViewById(R.id.tv_zzmm);
        tv_xi = findViewById(R.id.tv_xi);
        tv_zymc = findViewById(R.id.tv_zymc);
        tv_xzb = findViewById(R.id.tv_xzb);
        tv_dqszj = findViewById(R.id.tv_dqszj);
        tv_rxrq = findViewById(R.id.tv_rxrq);
        tv_xlcc = findViewById(R.id.tv_xlcc);
        tv_xxxs = findViewById(R.id.tv_xxxs);
        tv_xz = findViewById(R.id.tv_xz);
        tv_xjzt = findViewById(R.id.tv_xjzt);

        img = findViewById(R.id.iv_pic);
    }
}
