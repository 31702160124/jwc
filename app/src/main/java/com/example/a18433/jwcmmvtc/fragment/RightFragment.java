package com.example.a18433.jwcmmvtc.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.MyApplication;
import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.studentinfo_fragment;
import com.example.a18433.jwcmmvtc.fragment.other_fragment.xueshengchengji_fragment;

import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setHPicSrc;

public class RightFragment extends Fragment {
    private showPane slp;
    private TextView tx;
    private static studentinfo_fragment f1;
    private static xueshengchengji_fragment f2;
    private final static String TAG = "学生";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_fragment, null);
        init(view);
        return view;
    }

    public void init(View view) {
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            slp = (showPane) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                f1 = new studentinfo_fragment();
                f2 = new xueshengchengji_fragment();
            }
        }, 2000);
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
//                whatFragments(id);
                break;
            case 3:
//                whatFragments(id);
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void whatFragments(Fragment fragment) {
        if (fragment.isAdded()) {
            getFragmentManager().beginTransaction().show(fragment).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.other_fragment, fragment).commit();
        }
    }

    public interface showPane {
        void showPane();

        void loginOut();
    }

}
