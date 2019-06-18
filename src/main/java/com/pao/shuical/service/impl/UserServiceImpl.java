package com.pao.shuical.service.impl;

import com.pao.shuical.domain.User;
import com.pao.shuical.service.UserService;
import com.pao.shuical.util.ShuLogin;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User login(String userName,String passWord){
        int i = 1;
        do {
            localCookieJar.removeAll(); //这里把cookie清空了，因为cookiejar这里是静态的，我不想在登陆失败后还保存之前的cookie信息。
            System.out.println("正在尝试第"+i+++"次登录");
        }
        while (!ShuLogin.login(client,userName,passWord)&&i<6);  //如果登入失败只允许登入5次
        if(i==6)
            return null;  //5次登陆失败，出现了一些问题，可能是由于密码错误造成的。
        else
            return new User(userName,passWord);
    }
}