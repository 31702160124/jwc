package com.example.a18433.jwcmmvtc.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a18433.jwcmmvtc.Service.cookieService;
import com.example.a18433.jwcmmvtc.entity.kebiao;
import com.example.a18433.jwcmmvtc.entity.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class workFragment extends Fragment {
    static Bitmap bitmap;
    static ArrayList<user> dataList;
    static ArrayList<kebiao> kebiaoList;

    public static ArrayList<kebiao> getKebiaoList() {
        return kebiaoList;
    }

    public static void setKebiaoList(ArrayList<kebiao> kebiaoList) {
        workFragment.kebiaoList = kebiaoList;
    }

    static Map<String, String> map;

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static ArrayList<user> getDataList() {
        return dataList;
    }

    public static Map<String, String> getMap() {
        return map;
    }

    private static void setBitmap(Bitmap bitmap) {
        workFragment.bitmap = bitmap;
    }

    private static void setDataList(ArrayList<user> dataList) {
        workFragment.dataList = dataList;
    }

    private static void setMap(Map<String, String> map) {
        workFragment.map = map;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("workFra", "onCreate: workFragment");
        setRetainInstance(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("workrun", "run: ");
                try {
                    Bitmap bitmap = cookieService.getJwcdao().getHeadPic();
                    setBitmap(bitmap);
                    ArrayList<user> dataList = cookieService.getJwcdao().getZGrade();
                    setDataList(dataList);
                    Map<String, String> map = cookieService.getJwcdao().getPersonalInfo();
                    setMap(map);
                    ArrayList<kebiao> kebiaoList = cookieService.getJwcdao().getKeBiao();
                    setKebiaoList(kebiaoList);
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
        return null;
    }

}
