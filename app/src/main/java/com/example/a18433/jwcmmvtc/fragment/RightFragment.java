package com.example.a18433.jwcmmvtc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a18433.jwcmmvtc.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;

public class RightFragment extends Fragment {
    private showPane slp;
    private TextView tx;
    private ListView rlv;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> data;
    private String content;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_fragment, null);
        init(view);
        return view;
    }

    public void init(View view) {
        rlv = view.findViewById(R.id.lv_right);
        whatLeftId(4);
        tx = view.findViewById(R.id.title_tv);
        view.findViewById(R.id.right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slp.showPane();
            }
        });
        view.findViewById(R.id.login_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slp.loginOut();
            }
        });
    }

    public void setTite(String content) {
        tx.setText(content);
        this.content = content;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            slp = (showPane) context;
        } catch (Exception e) {

        }
    }

    public interface showPane {
        void showPane();

        void loginOut();
    }

    public void whatLeftId(Integer i) {
        switch (i) {
            case 0:
                arrayAdapter.clear();
                break;
            case 1:
                getData(content, i);
                arrayAdapter.notifyDataSetChanged();
                break;
            case 2:
                getData(content, i);
                arrayAdapter.notifyDataSetChanged();
                break;
            case 3:
                getData(content, i);
                arrayAdapter.notifyDataSetChanged();
                break;
            case 4:
                data = new ArrayList<>();
                data.add("欢迎你的到来" + getCookie());
                arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
                rlv.setAdapter(arrayAdapter);
                break;
        }
    }

    public void getData(String content, Integer i) {
        data.removeAll(data);
//        data = new ArrayList<>();
        for (i = 0; i <= 20; i++) {
            data.add(i + content);
        }
    }

}
