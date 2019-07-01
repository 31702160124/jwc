package com.example.a18433.jwcmmvtc.fragment.other_fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.entity.kebiao;

import java.util.ArrayList;

import static com.example.a18433.jwcmmvtc.fragment.workFragment.getKebiaoList;

public class xueshengkebiao_fragment extends Fragment {
    private ProgressDialog progressDialog;
    private SmartTable<Integer> table;
    private ArrayList<kebiao> dataList = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.xueshengkebiao_fragment, null);
        table = (SmartTable<Integer>) view.findViewById(R.id.kebiao_tv);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    do {
                        dataList = getKebiaoList();
                    } while (dataList == null);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 500);
                        initTable(dataList);
                    }
                });
            }
        }).start();
        setRetainInstance(true);
        return view;
    }

    /**
     * 初始化表格
     */
    private void initTable(ArrayList<kebiao> dataList) {
        Column<String> Time = new Column<>("Time", "Time");
        Column<String> Monday = new Column<>("Monday", "Monday");
        Column<String> Tuesday = new Column<>("Tuesday", "Tuesday");
        Column<String> Wednesday = new Column<>("Wednesday", "Wednesday");
        Column<String> Thursday = new Column<>("Thursday", "Thursday");
        Column<String> Friday = new Column<>("Friday", "Friday");
        Column<String> Saturday = new Column<>("Saturday", "Saturday");
        Column<String> Sunday = new Column<>("Sunday", "Sunday");

        Time.setFixed(true);
        TableData tableData = new TableData<kebiao>("", dataList,
                Time, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday);
        table.getConfig()
                .setContentStyle(new FontStyle(24, Color.rgb(68, 136, 187)))
                .setColumnTitleStyle(new FontStyle(24, Color.BLACK))
                .setHorizontalPadding(20)
                .setVerticalPadding(20);

        table.setTableData(tableData);
    }
}