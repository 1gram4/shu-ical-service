package com.pao.shuical.util;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import java.util.ArrayList;
import java.util.List;

/*
这是一个简单的cookiejar，没有持久化cookie,实现了自动装配cookie
 */
public class LocalCookieJar implements CookieJar {
    private List<Cookie> cookies;
    public LocalCookieJar(){
        cookies = new ArrayList<Cookie>();
    }
    @Override
    public List<Cookie> loadForRequest(HttpUrl arg0) {  //arg0是请求的url
        List<Cookie> cookiesTemp = new ArrayList<Cookie>();
        if (cookies != null){
            for(int i=0;i<cookies.size();i++){
                if(cookies.get(i).matches(arg0)){  //请求url与列表中cookie匹配就把它带上
                    cookiesTemp.add(cookies.get(i));
                }
            }
        }
        return cookiesTemp;
    }
    @Override
    public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
        this.cookies.addAll(cookies);//我这里把他们全部加进来了
    }
    public void add(Cookie cookie){
        this.cookies.add(cookie);
    }
    public void removeAll(){
        this.cookies.removeAll(this.cookies);
    }
}
