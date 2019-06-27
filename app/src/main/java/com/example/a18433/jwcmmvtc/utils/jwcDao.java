package com.example.a18433.jwcmmvtc.utils;

import android.util.Log;
import android.widget.Toast;

import com.example.a18433.jwcmmvtc.MainActivity;
import com.example.a18433.jwcmmvtc.MyApplication;
import com.example.a18433.jwcmmvtc.config.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.a18433.jwcmmvtc.config.Constant.jwccookieheader;
import static com.example.a18433.jwcmmvtc.config.Constant.jwcheader;
import static com.example.a18433.jwcmmvtc.config.Constant.loginfrom;
import static com.example.a18433.jwcmmvtc.config.Constant.loginoutfrom;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.addEerror;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.delEerror;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getLoginTime;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getUsername;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveIslogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setCookie;

public class jwcDao {
    private static volatile jwcDao jwcdao;
    private OkHttpClient client;
    private String ViewState = "";
    private String LogOutViewState = "";
    private String jwcCookie = "";
    // 用户名
    private String username;
    // 操作地址
    private String grxxUrl, cjcxUrl, mmxgUrl, bjkbcxUrl;
    // 头像
    private String HPicSrc;

    private jwcDao() {
        Log.i("jwcDao", "jwcDao:创建 ");
        client = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                String u = url.host();
                String urlStr = url.url().toString();
                if (urlStr.equals(Constant.baseurl)) {
                    cookieStore.put(u, cookies);
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                if (cookies != null) {
                    String session = cookies.get(0).toString();
                    jwcCookie = session.substring(0, session.indexOf(";"));
                }
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();
    }

    /**
     * 单例模式
     *
     * @return jwcDao
     */
    public static jwcDao getInstance() {
        if (jwcdao == null) {
            synchronized (jwcDao.class) {
                if (jwcdao == null) {
                    jwcdao = new jwcDao();
                }
            }
        }
        return jwcdao;
    }

    /**
     * 获取cookie是否过期
     */
    public Boolean cookieIsOverdue() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(Constant.cookieisoverUrl + getUsername())
                .addHeader(jwccookieheader[0], getCookie())
                .addHeader(jwccookieheader[2], jwccookieheader[3] + username)
                .build();
        String html = null;
        try {
            html = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Boolean isOverdue;
        String str1 = "Object moved"; //html.contains(str1)
        String str2 = "正方教务管理系统";//!html.contains(str2)
        if (!html.contains(str2)) {
            //cookie 失效
            isOverdue = false;
            setCookie("");
            addEerror();
            saveIslogin(false);
        } else {
            //cookie 可用
            saveIslogin(true);
            isOverdue = true;
        }
        return isOverdue;
    }

    /**
     * 获取验证码图片
     *
     * @return 图片数据
     * @throws IOException
     */
    public byte[] getCheckCodeImg() throws IOException {
        Request request = new Request.Builder().url(Constant.baseurl).build();
        Response response = client.newCall(request).execute();
        Document doc = Jsoup.parse(response.body().string());
        Elements elements = doc.getElementsByAttributeValue("name", "__VIEWSTATE");
        this.ViewState = elements.first().val();

        request = new Request.Builder().url(Constant.checkCodeUrl).build();
        ResponseBody body = client.newCall(request).execute().body();
        byte[] data = body.bytes();
        Headers headers = response.headers();
        List<String> cookies = headers.values("Set-Cookie");
        for (String str : cookies) {
            String s = str.substring(0, str.indexOf(";"));
            jwcCookie = s;
            Log.i("Set-Cookie", "getCheckCodeImg: " + s + "14");
        }
        return data;
    }

    /**
     * 登录
     *
     * @param user
     * @param pwd
     * @param checkCode
     * @return
     */
    public String[] Login(String user, String pwd, String checkCode) {
        username = user;
        if (ViewState.equals("")) {
            ViewState = loginfrom[1];
        }
        RequestBody body = new FormBody.Builder()
                .add(loginfrom[0], ViewState)
                .add(loginfrom[2], user)
                .add(loginfrom[4], pwd)
                .add(loginfrom[6], checkCode)
                .add(loginfrom[8], loginfrom[9])
                .add(loginfrom[10], loginfrom[11])
                .build();
        Request request = new Request.Builder()
                .url(Constant.loginurl)
                .addHeader(jwcheader[0], jwcheader[1])
                .addHeader(jwcheader[4], jwcCookie)
                .post(body)
                .build();
        String html = "";
        try {
            Response response = client.newCall(request).execute();
            html = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
            验证码不正确！！
            用户名不存在或未按照要求参加教学活动！！
            密码错误，您还有4次尝试机会！如忘记密码，请与教务处联系!
            密码错误，您密码输入错误已达5次，账号已锁定无法登录，次日自动解锁！如忘记密码，请与教务处联系!
         */

        String status, info;
        String[] rs = new String[2];
        if (html.contains("验证码不正确！！")) {
            status = "checkCode_error";
            info = "验证码不正确";
        } else if (html.contains("密码错误，您还有")) {
            status = "pwd_error";
            //使用正则
            String pattern = "密码错误.+?有(\\d+)次尝试机会";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(html);
            if (m.find()) {
                info = m.group();
            } else {
                info = "密码错误，请重新输入";
            }
        } else if (html.contains("错误已达5次，账号已锁定")) {
            status = "user_locked";
            info = "账号已锁定无法登录，次日自动解锁";
        } else if (html.contains("用户名不存在或未按照要求参加教学活动！！")) {
            status = "user_error";
            info = "用户名不存在";
        } else {
            status = "ok";
            setCookie(jwcCookie);
            saveIslogin(true);
            Document doc = Jsoup.parse(html);
            info = doc.selectFirst(".info > ul > li:first-child").text();
            this.queryURL(doc);
            Elements elements = doc.getElementsByAttributeValue("name", "__VIEWSTATE");
            this.LogOutViewState = elements.first().val();
        }

        rs[0] = status;
        rs[1] = info;

        return rs;
    }

    /**
     * 退出
     *
     * @return
     */
    public void jwcloginOut() {
        if (LogOutViewState.isEmpty()) {
            LogOutViewState = loginoutfrom[3];
        }
        RequestBody body = new FormBody.Builder()
                .add(loginoutfrom[0], loginoutfrom[1])
                .add(loginoutfrom[2], LogOutViewState)
                .add(loginoutfrom[4], loginoutfrom[5])
                .build();
        Request request = new Request.Builder()
                .url(Constant.loginOutUrl + username)
                .addHeader(jwcheader[0], jwcheader[1])
                .addHeader(jwcheader[2], jwcheader[3])
                .addHeader(jwcheader[4], getCookie())
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String html = response.body().string();
            if (html.contains("登录")) {
                setCookie("");
            } else {
                setCookie("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            delEerror();
        }
    }

    /**
     * 获取操作地址
     *
     * @param document
     */
    private void queryURL(Document document) {
        Elements eles = document.getElementsByAttributeValue("target", "zhuti");
        for (Element e : eles) {
            if (e.text().contains("个人信息")) {
                this.grxxUrl = e.attr("href");
                continue;
            }
            if (e.text().contains("密码修改")) {
                this.mmxgUrl = e.attr("href");
                continue;
            }
            if (e.text().contains("班级课表查询")) {
                this.bjkbcxUrl = e.attr("href");
                continue;
            }
            if (e.text().contains("学习成绩查询")) {
                this.cjcxUrl = e.attr("href");
            }
        }
    }
}
