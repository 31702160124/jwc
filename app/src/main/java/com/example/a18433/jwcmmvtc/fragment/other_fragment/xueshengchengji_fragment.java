package com.example.a18433.jwcmmvtc.fragment.other_fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.example.a18433.jwcmmvtc.MainActivity;
import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.entity.user;

import java.util.ArrayList;

public class xueshengchengji_fragment extends Fragment {
    private SmartTable<Integer> table;
    private ArrayList<user> dataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xueshengchengji_fragment, null);
        initTable(view);
        return view;
    }

    /**
     * 初始化表格
     */
    private void initTable(View view) {
        dataList = MainActivity.getDataList();
        table =  view.findViewById(R.id.table);
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
        term.setFixed(true);
        name.setFixed(true);
        credit.setFixed(true);
        score.setFixed(true);
        TableData tableData = new TableData<user>("", dataList
                , year, term, code, property, belong, credit, grade_point, score, minor_tag, retest_score,
                resume_score, college, note, rebuild_tag, name);
        table.getConfig()
                .setContentStyle(new FontStyle(24, Color.rgb(68, 136, 187)))
                .setColumnTitleStyle(new FontStyle(24, Color.BLACK));

        table.setTableData(tableData);
    }

}
