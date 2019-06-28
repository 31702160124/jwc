package com.example.a18433.jwcmmvtc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.a18433.jwcmmvtc.MyApplication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sharedPfUser {
    private static Context context = MyApplication.getContext();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void saveUserConfig(String username, String password) {
        sharedPreferences = context.getSharedPreferences("userConfig", 0);
        editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", Md5.JM(password));
        editor.commit();
    }

    public static Map getUserConfig() {
        Map map = new HashMap();
        sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String use = sharedPreferences.getString("username", "");
        String pwd = sharedPreferences.getString("password", "");
        map.put("username", use);
        map.put("password", Md5.JM(pwd));
        return map;
    }

    public static String getUsername() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String username = sharedPreferences.getString("username", "");
        return username;
    }

    public static Boolean saveIslogin(Boolean isLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.commit();
        return true;
    }

    public static Boolean userIsLogin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        Boolean islog = sharedPreferences.getBoolean("isLogin", false);
        return islog;
    }

    public static Boolean isFristlogin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        Boolean islog = sharedPreferences.getBoolean("isFristlogin", true);
        return islog;
    }

    public static void saversFristlogn(Boolean isFristlogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFristlogin", isFristlogin);
        editor.commit();
    }

    public static String getCookie() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String Cookie = sharedPreferences.getString("Cookie", "");
        return Cookie;
    }

    public static void setCookie(String Cookie) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Cookie", Cookie);
        editor.commit();
    }

    public static void addError() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Eerror", "Cookie过期");
        editor.commit();
    }

    public static void delError() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Eerror", "用户点击退出");
        editor.commit();
    }

    public static String getError() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String error = sharedPreferences.getString("Eerror", "用户点击退出");
        return error;
    }

    public static void setLoginTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("loginTime", new Date().getTime());
        editor.commit();
    }

    public static Long getLoginTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        Long LoginTime = sharedPreferences.getLong("loginTime", new Date().getTime());
        return LoginTime;
    }

    public static String getname() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String name = sharedPreferences.getString("name", "");
        return name;
    }

    public static void setname(String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static void setUrlbody(String i, String i1, String i2, String i3) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("grxxUrl", i);
        editor.putString("cjcxUrl", i1);
        editor.putString("mmxgUrl", i2);
        editor.putString("bjkbcxUrl", i3);
        editor.commit();
    }

    public static void delUrlbody() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("grxxUrl", "");
        editor.putString("cjcxUrl", "");
        editor.putString("mmxgUrl", "");
        editor.putString("bjkbcxUrl", "");
        editor.commit();
    }

    public static String getGrxxUrl() {
        sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String grxxUrl = sharedPreferences.getString("grxxUrl", "");
        return grxxUrl;
    }

    public static String getCjcxUrl() {
        sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String cjcxUrl = sharedPreferences.getString("cjcxUrl", "");
        return cjcxUrl;
    }

    public static String getMmxgUrl() {
        sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String mmxgUrl = sharedPreferences.getString("mmxgUrl", "");
        return mmxgUrl;
    }

    public static String getBjkbcxUrl() {
        sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String bjkbcxUrl = sharedPreferences.getString("bjkbcxUrl", "");
        return bjkbcxUrl;
    }

    public static String getHPicSrc() {
        sharedPreferences = context.getSharedPreferences("userConfig", 0);
        String HPicSrc = sharedPreferences.getString("photo", "");
        return HPicSrc;
    }

    public static void setHPicSrc(String HPicSrc) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userConfig", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("photo", HPicSrc);
        editor.commit();
    }

}
