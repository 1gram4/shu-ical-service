package com.pao.shuical.service.impl;

import com.pao.shuical.domain.Course;
import com.pao.shuical.domain.User;
import com.pao.shuical.service.UserService;
import com.pao.shuical.util.Crawler;
import com.pao.shuical.util.LocalCookieJar;
import com.pao.shuical.util.ShuUserUtil;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User login(String userName,String passWord){
        User user = new User(userName,passWord);
        ShuUserUtil shuUserUtil = ShuUserUtil.getInstance();
        final int LOGINSIZE = 1;
        int i = 0;
        do {
            user.getLocalCookieJar().removeAll(); //这里把cookie清空了，因为cookiejar这里是静态的，我不想在登陆失败后还保存之前的cookie信息。
            System.out.println("正在尝试第"+(++i)+"次登录");
            if (shuUserUtil.login(user)) {
                return user;
            }
        }
        while (i<LOGINSIZE);  //如果登入失败只允许登入1次
        return null;
    }
    @Override
    public List<Course> findCourses(User user){
        return Crawler.CrawlCourse(user);
    }
    @Override
    public Boolean logout(User user){
        ShuUserUtil shuUserUtil = ShuUserUtil.getInstance();
        return shuUserUtil.logout(user);
    }
}
