package com.example.a18433.jwcmmvtc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.changPwd_fragment;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.other_fragment;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.studentchenji_fragment;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.studentinfo_fragment;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.xueshengkebiao_fragment;

public class RightFragment extends Fragment {
    private showPane slp;
    private TextView tx;
    private studentinfo_fragment f1;
    private studentchenji_fragment f2;
    private xueshengkebiao_fragment f3;
    private changPwd_fragment f4;
    private other_fragment f0;
    private final static String TAG = "学生";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f0 = new other_fragment();
        f1 = new studentinfo_fragment();
        f2 = new studentchenji_fragment();
        f3 = new xueshengkebiao_fragment();
        f4 = new changPwd_fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_fragment, null);
        init(view);
        return view;
    }

    public void init(View view) {
        tx = (TextView) view.findViewById(R.id.title_tv);
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        slp = (showPane) context;
    }

    public void addFragments(int id) {
        switch (id) {
            case 0:
                whatFragments(f1);
                break;
            case 1:
                whatFragments(f2);
                break;
            case 2:
                whatFragments(f3);
                break;
            case 3:
                whatFragments(f4);
                break;
            case 4:
                whatFragments(f0);
                break;
        }
    }

    private void whatFragments(Fragment fragment) {
        if (fragment != null) {
            if (fragment.isAdded()) {
                Log.i(TAG, "whatFragments: add");
                getFragmentManager().beginTransaction().show(fragment).commit();
            } else {
                Log.i(TAG, "whatFragments: ");
                getFragmentManager().beginTransaction().replace(R.id.other_fragment, fragment).commit();
            }
        }
    }

    public interface showPane {
        void showPane();

        void loginOut();
    }

}
