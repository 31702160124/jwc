package com.example.a18433.jwcmmvtc.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.Service.cookieService;
import com.example.a18433.jwcmmvtc.config.Constant;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getname;

public class LeftFragment extends Fragment {
    private ListView lv;
    private closePane st;
    private ImageView img_tv;
    private TextView name;
    private Bitmap bitmap;
    private LinearLayout img_left_tv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        st = (closePane) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getCookie().isEmpty()) {
                        Thread.currentThread().wait();
                    } else {
                        bitmap = cookieService.getJwcdao().getHeadPic();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img_tv.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_fragment, null);
        init(view);
        return view;
    }

    private void init(View view) {
        name = view.findViewById(R.id.mane);
        name.setText(getname());
        img_left_tv = view.findViewById(R.id.img_Left_tv);
        img_left_tv.setBackgroundDrawable(Constant.getRandm(Constant.bgarray));
        img_tv = view.findViewById(R.id.Student_image);
        lv = view.findViewById(R.id.lv_left);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sInfo = parent.getItemAtPosition(position).toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cookieService.getJwcdao().cookieIsOverdue();
                    }
                }).start();
                switch (position) {
                    case 0:
                        ListViewCase(sInfo, position);
                        break;
                    case 1:
                        ListViewCase(sInfo, position);
                        break;
                    case 2:
                        ListViewCase(sInfo, position);
                        break;
                    case 3:
                        ListViewCase(sInfo, position);
                        break;
                    default:
                        ListViewCase("教务管理系统", 4);
                        break;
                }
            }
        });

    }

    private void ListViewCase(String content, int id) {
        st.rightStates(content, id);
    }

    public interface closePane {
        void rightStates(String content, int id);
    }

}
