package com.example.a18433.jwcmmvtc.config;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.a18433.jwcmmvtc.MyApplication;
import com.example.a18433.jwcmmvtc.R;

public class Constant {

    public final static String baseurl = "http://jwc.mmvtc.cn/";

    public static int[] catarray = {R.drawable.cat0, R.drawable.cat1, R.drawable.cat2,
            R.drawable.cat3, R.drawable.cat4, R.drawable.cat5, R.drawable.cat6};

    public static int[] loginarray = {R.drawable.login0, R.drawable.login1, R.drawable.login2,
            R.drawable.login3, R.drawable.login4, R.drawable.login5, R.drawable.login6, R.drawable.login7};

    public static int[] bgarray = {R.drawable.bg0, R.drawable.bg1, R.drawable.bg2,
            R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6, R.drawable.bg7};

    public static Drawable getRandm(int[] array) {
        int random = (int) (Math.random() * 100 % 10);
        int i = random;
        while (random > array.length - 1) {
            if (random == i) {
                random = (int) (Math.random() * 100 % 10);
            }
        }
        int id = array[random];
        Log.i("随机数", "getRandm: " + random);
        Drawable drawable = MyApplication.getContext().getResources().getDrawable(id);
        return drawable;
    }

    //登入
    public final static String checkCodeUrl = "http://jwc.mmvtc.cn/CheckCode.aspx";
    public final static String loginurl = "http://jwc.mmvtc.cn/default2.aspx";
    public final static String[] loginfrom = new String[]{
            "__VIEWSTATE", "dDw3OTkxMjIwNTU7Oz5qFv56B08dbR82AMSOW+P8WDKexA==",
            "TextBox1", "",
            "TextBox2", "",
            "TextBox3", "",
            "RadioButtonList1", "学生",
            "Button1", "",};
    public final static String[] jwcheader = new String[]{
            "Content-Type", "application/x-www-form-urlencoded",
            "Referer", "http://jwc.mmvtc.cn/xscjcx.aspx?xh=",
            "Cookie", ""};
    //退出
    public final static String loginOutUrl = "http://jwc.mmvtc.cn/xs_main.aspx?xh=";
    public final static String[] loginoutfrom = new String[]{
            "__EVENTTARGET", "likTc",
            "__VIEWSTATE", "dDwtMTM0NTkyMTI1NDs7Pvc/28t3Ic9R5SInH5252nziPfHq",
            "__EVENTARGUMENT", ""};

    //判断cookie是否过期
    public final static String cookieisoverUrl = "http://jwc.mmvtc.cn/xs_main.aspx?xh=";
    public final static String[] jwccookieheader = new String[]{
            "Cookie", "",
            "Referer", "http://jwc.mmvtc.cn/xscjcx.aspx?xh="};
}
