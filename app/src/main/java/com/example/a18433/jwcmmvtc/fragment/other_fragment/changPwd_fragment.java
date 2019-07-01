package com.example.a18433.jwcmmvtc.fragment.other_fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.config.Constant;

/**
 * Created by Administrator on 2019/7/1.
 */

public class changpwd_fragment extends Fragment {
    private EditText old_pwd, new_pwd1, new_pwd2;
    private String old_pwdstr, new_pwd1str, new_pwd2str;
    private Button chang_pwd;
    private LinearLayout chang_pwd_tv;
    private tuichu tuichu;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tuichu = (changpwd_fragment.tuichu) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changpwd_fragment, null);
        setRetainInstance(true);
        init(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init(View view) {
        chang_pwd_tv = (LinearLayout) view.findViewById(R.id.chang_pwd_tv);
        chang_pwd_tv.setBackground(Constant.getRandm(Constant.loginarray));
        old_pwd = (EditText) view.findViewById(R.id.old_pwd);
        new_pwd1 = (EditText) view.findViewById(R.id.new_pwd1);
        new_pwd2 = (EditText) view.findViewById(R.id.new_pwd2);
        chang_pwd = (Button) view.findViewById(R.id.chang_pwd);
        chang_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.i("onFocusChange", "onFocusChange: " + view.getId() + hasFocus);
                InputMethodManager manager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if (hasFocus) {
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        changPed();
                    }
                } else {
                    manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    private void changPed() {
        old_pwdstr = old_pwd.getText().toString().trim();
        new_pwd1str = new_pwd1.getText().toString().trim();
        new_pwd2str = new_pwd2.getText().toString().trim();
        // 判断参数是否为空
        if (old_pwdstr.equals("")) {
            showAlertDialog("请输入原密码");
            //            Toast.makeText(getActivity(), "请输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new_pwd1str.equals("") | new_pwd2str.equals("")) {
            showAlertDialog("请输入新密码");
            //            Toast.makeText(getActivity(), "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new_pwd1str.equals(new_pwd2str)) {
            if (new_pwd1str.length() < 6 | new_pwd2str.length() < 6) {
                showAlertDialog("密码小于6位");
                return;
            }
            if (new_pwd1str.equals("123456") | new_pwd1str.equals("000000")) {
                showAlertDialog("密码不能为“123456”或“000000”");
                //                Toast.makeText(getActivity(), "密码不能为“123456”或“000000”", Toast.LENGTH_SHORT).show();
                return;
            }
            tuichu.tuichu();
        } else {
            showAlertDialog("两次新密码不相等");
            return;
            //            Toast.makeText(getActivity(), "两次新密码不相等", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog(String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(string);     //设置对话框标题

        //        builder.setIcon(R.drawable.ic_launcher);      //设置对话框标题前的图标

        builder.setPositiveButton("确认", null);

        builder.setCancelable(false);   //设置按钮是否可以按返回键取消,false则不可以取消

        AlertDialog dialog = builder.create();  //创建对话框

        dialog.setCanceledOnTouchOutside(false);      //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏

        dialog.show();
    }

    public interface tuichu {
        void tuichu();
    }
}
