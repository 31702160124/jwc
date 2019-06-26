package com.example.httpdemo;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.example.httpdemo.Bean.Grade;
import com.example.httpdemo.Utils.Tools;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    private SmartTable<Integer> table;
    private Tools tools;
    private ArrayList<Grade> dataList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        table = (SmartTable<Integer>) findViewById(R.id.table);

        tools = Tools.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("查询中");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                dataList = tools.getZGrade();
                if (dataList.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                        }
                    });

                    Toast.makeText(ScoreActivity.this, "获取历年成绩失败",
                            Toast.LENGTH_SHORT).show();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 初始化表格
                            initTable();
                            progressDialog.hide();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 初始化表格
     */
    private void initTable() {
        Column<String> year = new Column<>("学年", "year");
        Column<String> term = new Column<>("学期", "term");
        Column<String> code = new Column<>("课程代码", "code");
        Column<String> name = new Column<>("课程名称", "name");
        Column<String> property = new Column<>("课程性质", "property");
        Column<String> belong = new Column<>("课程归属", "belong");
        Column<String> credit = new Column<>("学分", "credit");
        Column<String> grade_point = new Column<>("绩点", "grade_point");
        Column<String> score = new Column<>("成绩", "score");
        Column<String> minor_tag = new Column<>("辅修标记", "minor_tag");
        Column<String> retest_score = new Column<>("补考成绩", "retest_score");
        Column<String> resume_score = new Column<>("重修成绩", "resume_score");
        Column<String> college = new Column<>("开课学院", "college");
        Column<String> note = new Column<>("备注", "note");
        Column<String> rebuild_tag = new Column<>("重修标记", "rebuild_tag");

        // 固定列
        name.setFixed(true);
        TableData tableData = new TableData<>("历年成绩表", dataList
                , year, term, code, name, property, belong, credit, grade_point, score, minor_tag, retest_score,
                resume_score, college, note, rebuild_tag);
        table.getConfig()
                .setContentStyle(new FontStyle(24, Color.rgb(68, 136, 187)))
                .setColumnTitleStyle(new FontStyle(24, Color.BLACK))
                .setTableTitleStyle(new FontStyle(30, Color.rgb(95, 116, 133)));

        table.setTableData(tableData);
    }
}
