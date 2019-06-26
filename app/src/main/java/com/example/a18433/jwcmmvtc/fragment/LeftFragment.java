package com.example.a18433.jwcmmvtc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.Service.cookieService;


public class LeftFragment extends Fragment {
    ListView lv;
    closePane st;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_fragment, null);
        init(view);
        return view;
    }

    private void init(View view) {
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
                        st.closePane(sInfo);
                        rightFaStates(position);
                        break;
                    case 1:
                        st.closePane(sInfo);
                        rightFaStates(position);
                        break;
                    case 2:
                        st.closePane(sInfo);
                        rightFaStates(position);
                        break;
                    case 3:
                        st.closePane(sInfo);
                        rightFaStates(position);
                        break;
                }
            }
        });
    }

    public void rightFaStates(Integer position) {
        RightFragment r = (RightFragment) getFragmentManager().findFragmentById(R.id.right_fragment);
        r.whatLeftId(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        st = (closePane) context;
    }

    public interface closePane {
        void closePane(String content);
    }
}
