package com.pao.shuical.service;

import com.pao.shuical.domain.Course;
import com.pao.shuical.domain.User;
import com.pao.shuical.util.LocalCookieJar;
import okhttp3.OkHttpClient;

import java.util.List;

public interface UserService {
    public static LocalCookieJar localCookieJar = new LocalCookieJar();
    public static OkHttpClient client = new OkHttpClient().newBuilder()
            // .followRedirects(false)    //禁止自动重定向！！！
            .cookieJar(localCookieJar)   //为OkHttp设置自动携带Cookie的功能
            .build();
    public User login(String userName,String PassWord);
    public List<Course> findCourses(String userName);
}
