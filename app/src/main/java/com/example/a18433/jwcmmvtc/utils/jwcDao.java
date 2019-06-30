package com.example.a18433.jwcmmvtc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.util.Log;

import com.example.a18433.jwcmmvtc.MyApplication;
import com.example.a18433.jwcmmvtc.R;
import com.example.a18433.jwcmmvtc.config.Constant;
import com.example.a18433.jwcmmvtc.entity.kebiao;
import com.example.a18433.jwcmmvtc.entity.user;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.addError;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.delError;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.delUrlbody;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getBjkbcxUrl;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCjcxUrl;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getGrxxUrl;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getHPicSrc;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.getUsername;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.saveIslogin;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setCookie;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setHPicSrc;
import static com.example.a18433.jwcmmvtc.utils.sharedPfUser.setUrlbody;

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
        Log.i("检查cookie", "cookieIsOverdue: ");
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
        if (html.contains(str1)) {
            //cookie 失效
            isOverdue = false;
            setCookie("");
            delUrlbody();
            addError();
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
    public Bitmap getCheckCodeImg() throws IOException {
        Request request = new Request.Builder().url(Constant.baseurl).build();
        Response response = client.newCall(request).execute();
        this.ViewState = findViewState(response.body().string());

        request = new Request.Builder().url(Constant.checkCodeUrl).build();
        byte[] body = client.newCall(request).execute().body().bytes();
        Bitmap data = BitmapFactory.decodeByteArray(body, 0, body.length);
        Headers headers = response.headers();
        List<String> cookies = headers.values("Set-Cookie");
        for (String str : cookies) {
            String s = str.substring(0, str.indexOf(";"));
            jwcCookie = s;
            Log.i("Set-Cookie", "getCheckCodeImg: " + s + "14");
        }
        return data;
    }

    private String findViewState(String html) {
        if (html.equals("")) {
            return "";
        }
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByAttributeValue("name", "__VIEWSTATE");
        return elements.first().val();
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
            setCookie("");
            setHPicSrc("");
            delUrlbody();
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
        setUrlbody(grxxUrl, cjcxUrl, mmxgUrl, bjkbcxUrl);
    }

    /**
     * 获取个人信息
     */
    public Map<String, String> getPersonalInfo() {
        String url = Constant.baseurl + getGrxxUrl();
        Request request = new Request.Builder().url(url)
                .addHeader(jwcheader[2], jwcheader[3] + username)
                .addHeader(jwcheader[4], getCookie())
                .build();

        String html = "";
        try {
            Response response = client.newCall(request).execute();
            html = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.parse(html);
        // 部分个人信息 用于显示在手机界面上
        String xh = doc.getElementById("xh").text();
        String xm = doc.getElementById("xm").text();
        String sex = doc.getElementById("lbl_xb").text();
        String zzmm = doc.getElementById("lbl_zzmm").text();
        String xi = doc.getElementById("lbl_xi").text();
        String zymc = doc.getElementById("lbl_zymc").text();
        String xzb = doc.getElementById("lbl_xzb").text();
        String dqszj = doc.getElementById("lbl_dqszj").text();
        String rxrq = doc.getElementById("lbl_rxrq").text();
        String xlcc = doc.getElementById("lbl_CC").text();
        String xxxs = doc.getElementById("lbl_xxxs").text();
        String xz = doc.getElementById("lbl_xz").text();
        String xjzt = doc.getElementById("lbl_xjzt").text();

        // 学生照片地址
        HPicSrc = Constant.baseurl + doc.getElementById("xszp").attr("src");
        setHPicSrc(HPicSrc);

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("xh", xh);
        infoMap.put("xm", xm);
        infoMap.put("sex", sex);
        infoMap.put("zzmm", zzmm);
        infoMap.put("xi", xi);
        infoMap.put("zymc", zymc);
        infoMap.put("xzb", xzb);
        infoMap.put("rxrq", rxrq);
        infoMap.put("xz", xz);
        infoMap.put("xjzt", xjzt);
        infoMap.put("dqszj", dqszj);
        infoMap.put("xlcc", xlcc);
        infoMap.put("xxxs", xxxs);
        return infoMap;
    }

    /**
     * 获取照片数据
     *
     * @return byte[]
     * @throws IOException
     */
    public Bitmap getHeadPic() throws IOException {

        if (getHPicSrc().isEmpty()) {
            getPersonalInfo();
        }

        Request request = new Request.Builder()
                .addHeader(jwcheader[4], getCookie())
                .url(getHPicSrc()).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        byte[] bytes = body.bytes();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * 获取历年成绩
     */
    public ArrayList<user> getZGrade() {

        String strHTML = "";
        // GET 学生成绩查询页面 得到 __VIEWSTATE
        Request request = new Request.Builder()
                .url(Constant.baseurl + getCjcxUrl())
                .addHeader(jwcheader[2], jwcheader[3])
                .addHeader(jwcheader[4], getCookie())
                .build();
        try {
            Response response = client.newCall(request).execute();
            strHTML = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String vs = findViewState(strHTML);
        // 如果获取不到vs值 则采用固定的值
        if (vs.equals("")) {
            vs = "dDwtMTAyNzg2MDg2Njt0PHA8bDx4aDtkeWJ5c2NqO3p4Y2pjeHhzO3NmZGNiaztTb3J0RXhwcmVzO1NvcnREaXJlO2RnMztzdHJfdGFiX2JqZzs+O2w8MzE3MDIxNjAxMTE7XGU7MDtcZTtrY21jO2FzYztiamc7emZfY3hjanRqXzMxNzAyMTYwMTExOz4+O2w8aTwxPjs+O2w8dDw7bDxpPDI+O2k8Nz47aTwyMz47aTwyOT47aTwzMT47aTwzMz47aTwzNT47aTwzNj47aTwzOD47aTw0MD47aTw0Mj47aTw0ND47aTw0Nj47aTw0OD47aTw1MD47aTw1Mj47aTw1ND47aTw1Nj47aTw1Nz47aTw1OT47aTw2MT47aTw2Mz47aTw2NT47aTw2Nz47PjtsPHQ8dDw7dDxpPDIwPjtAPFxlOzIwMDEtMjAwMjsyMDAyLTIwMDM7MjAwMy0yMDA0OzIwMDQtMjAwNTsyMDA1LTIwMDY7MjAwNi0yMDA3OzIwMDctMjAwODsyMDA4LTIwMDk7MjAwOS0yMDEwOzIwMTAtMjAxMTsyMDExLTIwMTI7MjAxMi0yMDEzOzIwMTMtMjAxNDsyMDE0LTIwMTU7MjAxNS0yMDE2OzIwMTYtMjAxNzsyMDE3LTIwMTg7MjAxOC0yMDE5OzIwMTktMjAyMDs+O0A8XGU7MjAwMS0yMDAyOzIwMDItMjAwMzsyMDAzLTIwMDQ7MjAwNC0yMDA1OzIwMDUtMjAwNjsyMDA2LTIwMDc7MjAwNy0yMDA4OzIwMDgtMjAwOTsyMDA5LTIwMTA7MjAxMC0yMDExOzIwMTEtMjAxMjsyMDEyLTIwMTM7MjAxMy0yMDE0OzIwMTQtMjAxNTsyMDE1LTIwMTY7MjAxNi0yMDE3OzIwMTctMjAxODsyMDE4LTIwMTk7MjAxOS0yMDIwOz4+Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPGtjeHptYztrY3h6ZG07Pj47Pjt0PGk8NT47QDzlv4Xkv67or7475LiT5Lia6YCJ5L+u6K++O+S7u+aEj+mAieS/ruivvjvmoKHkvIHlvIDlj5Hor747XGU7PjtAPDAxOzAyOzAzOzA0O1xlOz4+Oz47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOWtpuWPt++8mjMxNzAyMTYwMTExO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85aeT5ZCN77ya6ZmI5aWV5qGmO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85a2m6Zmi77ya6K6h566X5py65bel56iL57O7O288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85LiT5Lia77yaO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w86L2v5Lu25oqA5pyvO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w86KGM5pS/54+t77yaMTfnp7vliqjkupLogZQxO288dD47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtsPGk8MD47aTwxPjs+O2w8dDw7bDxpPDA+Oz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47Pj47dDw7bDxpPDA+Oz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47dDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtsPGk8MD47PjtsPHQ8O2w8aTwwPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjs+Pjt0PHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O2w8aTwwPjs+O2w8dDw7bDxpPDA+O2k8MT47PjtsPHQ8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47dDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtsPGk8MD47aTwxPjs+O2w8dDw7bDxpPDA+Oz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47Pj47dDw7bDxpPDA+Oz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47dDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtsPGk8MD47PjtsPHQ8O2w8aTwwPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjs+Pjt0PHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O2w8aTwwPjtpPDE+O2k8Mj47aTwzPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85pys5LiT5Lia5YWxMjQ45Lq6O288Zj47Pj47Pjs7Pjs+Pjt0PDtsPGk8MD47PjtsPHQ8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs+Oz4+O3Q8O2w8aTwwPjs+O2w8dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47Pj47dDw7bDxpPDA+Oz47bDx0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjs+Pjs+Pjt0PHA8cDxsPFRleHQ7PjtsPHpmOz4+Oz47Oz47dDxwPHA8bDxJbWFnZVVybDs+O2w8Li9leGNlbC85NzA1MjcwLmpwZzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzoh7Pku4rmnKrpgJrov4for77nqIvmiJDnu6nvvJo7bzx0Pjs+Pjs+Ozs+O3Q8QDA8cDxwPGw8UGFnZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYXRhS2V5czs+O2w8aTwxPjtpPDA+O2k8MD47bDw+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpibG9jazs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPHQ+Oz4+Oz47Ozs7Ozs7Ozs7Pjs7Pjs+Pjs+Pjs+tA32flyhvuUmh7aAtanH9ixN/O8=";
        }

        // POST
        RequestBody body = new FormBody.Builder()
                .add("__EVENTTARGET", "")
                .add("__EVENTARGUMENT", "")
                .add("__VIEWSTATE", vs)
                .add("ddlXN", "")
                .add("ddlXQ", "")
                .add("ddl_kcxz", "")
                .add("btn_zcj", "历年成绩")
                .build();
        Request request2 = new Request.Builder()
                .url(Constant.baseurl + getCjcxUrl())
                .addHeader(jwcheader[0], jwcheader[1])
                .addHeader(jwcheader[2], jwcheader[3])
                .addHeader(jwcheader[4], getCookie()).post(body)
                .build();
        try {
            Response response = client.newCall(request2).execute();
            strHTML = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dealWithGradeHtml(strHTML);
    }

    /**
     * 处理成绩 form html
     *
     * @param grade_html
     */
    private ArrayList<user> dealWithGradeHtml(String grade_html) {
        Document doc = Jsoup.parse(grade_html);
        // 从第二个tr开始
        Elements esTr = doc.select(".datelist tr:nth-child(n+2)");

        ArrayList<user> gradeList = new ArrayList<user>();

        for (Element element : esTr) {
            Elements esTd = element.getElementsByTag("td");
            if (esTd.size() == 15) {
                String temp[] = new String[15];
                int i = 0;
                for (Element e : esTd) {
                    temp[i] = e.text();
                    i++;
                }
                user grade = new user();
                grade.setYear(temp[0]);
                grade.setTerm(temp[1]);
                grade.setCode(temp[2]);
                grade.setName(temp[3]);
                grade.setProperty(temp[4]);
                grade.setBelong(temp[5]);
                grade.setCredit(temp[6]);
                grade.setGrade_point(temp[7]);
                grade.setScore(temp[8]);
                grade.setMinor_tag(temp[9]);
                grade.setRetest_score(temp[10]);
                grade.setResume_score(temp[11]);
                grade.setCollege(temp[12]);
                grade.setNote(temp[13]);
                grade.setRebuild_tag(temp[14]);

                gradeList.add(grade);
            }
        }

//        Log.i("tag", "dealWithGradeHtml-list-size: " + gradeList.size());
        return gradeList;
    }

    /**
     * 获取个人课表
     */
    public ArrayList<kebiao> getKeBiao() {

        String strHTML = "";
        Request request = new Request.Builder()
                .url(Constant.baseurl + getBjkbcxUrl())
                .addHeader(jwcheader[2], jwcheader[3])
                .addHeader(jwcheader[4], getCookie())
                .build();
        try {
            Response response = client.newCall(request).execute();
            strHTML = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dealWithKebiaoHtml(strHTML);
    }

    private ArrayList<kebiao> dealWithKebiaoHtml(String kebiao_html) {
        Document doc = Jsoup.parse(kebiao_html);
        // 从第二个tr开始
        Elements esTr = doc.select(".blacktab tr:nth-child(n+3)");
        ArrayList<kebiao> kebiaoList = new ArrayList<kebiao>();
        for (Element element : esTr) {
            Elements esTd = element.getElementsByTag("td");
            String temp[] = new String[7];
            int j = 0;
            for (Element e : esTd) {
                if (e.text().indexOf("第") != -1) {
                    continue;
                }
                if (e.text().indexOf("上") != -1) {
                    continue;
                }
                if (e.text().indexOf("下") != -1) {
                    continue;
                }
                if (e.text().indexOf("晚") != -1) {
                    continue;
                }
                temp[j] = e.text();
                j++;
            }
            kebiao kebiao = new kebiao();
            kebiao.setMonday(temp[0]);
            kebiao.setTuesday(temp[1]);
            kebiao.setWednesday(temp[2]);
            kebiao.setThursday(temp[3]);
            kebiao.setFriday(temp[4]);
            kebiao.setSaturday(temp[5]);
            kebiao.setSunday(temp[6]);
            kebiaoList.add(kebiao);
        }
        return kebiaoList;
    }
}
