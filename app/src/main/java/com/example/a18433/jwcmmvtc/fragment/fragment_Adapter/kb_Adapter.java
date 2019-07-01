package com.example.a18433.jwcmmvtc.fragment.fragment_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.entity.kebiao;

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
        return mDataLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(R.layout.kb_row, null);
        LinearLayout row = (LinearLayout) view.findViewById(R.id.kb_row);  // 行布局
        if (row != null) row.removeAllViews(); // 清空行
        for (int i = 1; i <= mColumnNum; i++) {
            for (int t = 0; t < getkebiao(mDataLists).get(t).size(); t++) {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.kb_item, null);
                TextView itemNameTv = (TextView) itemView.findViewById(R.id.t_tv);
                itemNameTv.setText(getkebiao(mDataLists).get(i).get(t));
                itemNameTv.setEms(9);
                itemNameTv.setWidth(250);
                // 将每个元素添加到行布局中去
                row.addView(itemView);
                Log.i("课表是", "getView: "+getkebiao(mDataLists).get(i).get(t));
            }
        }
        return view;
    }
}
