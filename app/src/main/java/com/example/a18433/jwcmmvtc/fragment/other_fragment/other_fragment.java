package com.example.a18433.jwcmmvtc.fragment.other_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.config.Constant;

public class other_fragment extends Fragment {
    private ImageView img_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_fragment, null);
        img_tv = (ImageView) view.findViewById(R.id.img_tv);
        img_tv.setImageDrawable(Constant.getRandm(Constant.catarray));
        return view;
    }

}
