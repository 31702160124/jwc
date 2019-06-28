package com.example.a18433.jwcmmvtc.fragment.other_fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a18433.jwcmmvtc.MainActivity;
import com.example.a18433.jwcmmvtc.R;

import java.util.Map;

public class studentinfo_fragment extends Fragment {
    private TextView tv_xh, tv_xm, tv_sex, tv_zzmm, tv_xi, tv_zymc, tv_xzb, tv_dqszj, tv_rxrq, tv_xlcc, tv_xxxs, tv_xz, tv_xjzt;
    private ImageView img;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xueshengxingxi_fragment, null);
        init(view);
        return view;
    }

    private void init(View view) {
        Map<String, String> map = MainActivity.getMap();
        Bitmap bitmap = MainActivity.getBitmap();
        tv_xh = view.findViewById(R.id.tv_xh);
        tv_xm = view.findViewById(R.id.tv_xm);
        tv_sex = view.findViewById(R.id.tv_sex);
        tv_zzmm = view.findViewById(R.id.tv_zzmm);
        tv_xi = view.findViewById(R.id.tv_xi);
        tv_zymc = view.findViewById(R.id.tv_zymc);
        tv_xzb = view.findViewById(R.id.tv_xzb);
        tv_dqszj = view.findViewById(R.id.tv_dqszj);
        tv_rxrq = view.findViewById(R.id.tv_rxrq);
        tv_xlcc = view.findViewById(R.id.tv_xlcc);
        tv_xxxs = view.findViewById(R.id.tv_xxxs);
        tv_xz = view.findViewById(R.id.tv_xz);
        tv_xjzt = view.findViewById(R.id.tv_xjzt);
        img = view.findViewById(R.id.iv_pic);
        tv_xh.setText(map.get("xh"));
        tv_xm.setText(map.get("xm"));
        tv_sex.setText(map.get("sex"));
        tv_zzmm.setText(map.get("zzmm"));
        tv_xi.setText(map.get("xi"));
        tv_zymc.setText(map.get("zymc"));
        tv_xzb.setText(map.get("xzb"));
        tv_dqszj.setText(map.get("dqszj"));
        tv_rxrq.setText(map.get("rxrq"));
        tv_xlcc.setText(map.get("xlcc"));
        tv_xxxs.setText(map.get("xxxs"));
        tv_xz.setText(map.get("xz"));
        tv_xjzt.setText(map.get("xjzt"));
        img.setImageBitmap(bitmap);
    }

}
