package com.example.httpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_info, btn_zcj;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String hintInfo = intent.getStringExtra("info");
        TextView tv_info = findViewById(R.id.tv_info);
        tv_info.setText(hintInfo);

        btn_info = findViewById(R.id.btn_info);
        btn_zcj = findViewById(R.id.btn_zcj);

        btn_info.setOnClickListener(this);
        btn_zcj.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zcj:
                Intent intent = new Intent(this, ScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_info:
                Intent infoIntent = new Intent(this, PersonalInfoActivity.class);
                startActivity(infoIntent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
