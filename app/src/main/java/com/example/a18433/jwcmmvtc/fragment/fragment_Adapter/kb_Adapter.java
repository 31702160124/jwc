package com.example.a18433.jwcmmvtc.fragment.fragment_Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.entity.kebiao;

import java.util.ArrayList;
import java.util.List;

import static com.example.a18433.jwcmmvtc.entity.kebiao.getkebiao;

public class kb_Adapter extends BaseAdapter {

    private Context mContext;
    private List<kebiao> mDataLists;
    private int mColumnNum = 0;

    public kb_Adapter() {
    }

    /**
     * 构造函数
     *
     * @param mContext
     * @param mDataLists
     */
    public kb_Adapter(Context mContext, List<kebiao> mDataLists, int columnNum) {
        this.mContext = mContext;
        this.mDataLists = mDataLists;
        this.mColumnNum = columnNum;
    }

    @Override
    public int getCount() {
        return mDataLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.kb_row, null);
        final LinearLayout row = (LinearLayout) view.findViewById(R.id.kb_row);  // 行布局
        row.setId(position);
        if (row != null) row.removeAllViews(); // 清空行
        for (int i = 0; i < mColumnNum; i++) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.kb_item, null);
            final TextView itemNameTv = (TextView) itemView.findViewById(R.id.t_tv);
            itemNameTv.setText(getkebiao((ArrayList<kebiao>) mDataLists, position).get(i));
            itemNameTv.setEms(9);
            itemNameTv.setWidth(250);
            final int titlrid = Integer.valueOf(row.getId() + "" + i);
            itemNameTv.setId(titlrid);
            if (position == 1)
                itemNameTv.setHeight(90);
            else
                itemNameTv.setHeight(250);
            int id = Integer.valueOf(position + "" + 0);
            if (itemNameTv.getId() == id) {
                itemNameTv.setWidth(110);
            }
            itemNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("课表", position + "onClick: " + itemNameTv.getId());
                }
            });
            // 将每个元素添加到行布局中去
            if (position == 0)
                row.removeAllViews();
            else
                row.addView(itemView);
        }
        return view;
    }

}
