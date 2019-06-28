package com.example.a18433.jwcmmvtc.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a18433.jwcmmvtc.MainActivity;
import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.Service.cookieService;

import java.io.IOException;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getname;

public class LeftFragment extends Fragment {
    private ListView lv;
    private closePane st;
    private ImageView img_tv;
    private TextView name;
    private Bitmap bitmap;

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
                    bitmap = cookieService.getJwcdao().getHeadPic();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img_tv.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_fragment, null);
        init(view);
        return view;
    }

    private void init(View view) {
        name = view.findViewById(R.id.mane);
        name.setText(getname());
        img_tv = view.findViewById(R.id.Student_image);
        lv = view.findViewById(R.id.lv_left);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("RestrictedApi")
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
                }
            }
        });

    }

    private void ListViewCase(String content, int id) {
        st.rightFaStates(id);
        st.closePane(content);
    }

    public interface closePane {
        void closePane(String content);

        void rightFaStates(int id);
    }

}
