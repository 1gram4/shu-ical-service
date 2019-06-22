package com.pao.shuical.service;

import com.pao.shuical.domain.Course;
import com.pao.shuical.domain.User;
import com.pao.shuical.util.LocalCookieJar;
import okhttp3.OkHttpClient;

import java.util.List;

public interface UserService {
    public User login(String userName,String PassWord);
    public Boolean logout(User user);
    public List<Course> findCourses(User user);
}
