package com.example.a18433.jwcmmvtc.fragment.other_fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.entity.kebiao;
import com.example.a18433.jwcmmvtc.fragment.fragment_Adapter.kb_Adapter;

import java.util.ArrayList;

import static com.example.a18433.jwcmmvtc.entity.kebiao.getkebiao;
import static com.example.a18433.jwcmmvtc.fragment.workFragment.getKebiaoList;

public class xueshengkebiao_fragment extends Fragment {
    private ProgressDialog progressDialog;
    private ListView ls_kb;
    private ArrayList<kebiao> dataList = null;
    private LinearLayout top_kb;
    private kb_Adapter kb_adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.xueshengkebiao_fragment, null);
        top_kb = (LinearLayout) view.findViewById(R.id.top_kb);
        ls_kb = (ListView) view.findViewById(R.id.ls_kb);
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
//        setRetainInstance(true);
        return view;
    }

    private void initTable(ArrayList<kebiao> dataList) {
        for (int t = 0; t < getkebiao(dataList, 0).size(); t++) {
            View tp_tv = LayoutInflater.from(getContext()).inflate(R.layout.kb_top, null);
            TextView title = (TextView) tp_tv.findViewById(R.id.t_title);
            title.setText(getkebiao(dataList, 0).get(t));
            title.setEms(9);
            if (t == 0)
                title.setWidth(110);
            else
                title.setWidth(250);
            title.setHeight(100);
            top_kb.addView(tp_tv);
        }
        kb_adapter = new kb_Adapter(getContext(), dataList, 8);
        ls_kb.setAdapter(kb_adapter);
        Log.i("课表", "initTable: " + dataList.size() + dataList.get(0).toString());
    }
}