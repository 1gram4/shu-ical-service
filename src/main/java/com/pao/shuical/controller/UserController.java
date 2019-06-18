package com.pao.shuical.controller;

import com.pao.shuical.domain.User;
import com.pao.shuical.service.UserService;
import com.pao.shuical.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.SQLOutput;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    @Autowired
    private CommonResult commonResult;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/login")
    public CommonResult login(@RequestBody User loginUser, HttpSession httpSession){
        User user = userService.login(loginUser.getUserName(),loginUser.getPassWord());
        if(user == null){
            commonResult.status = -1;
            commonResult.result = "登入失败,账号或密码错误";
        }
        else{
            commonResult.status = 0;
            commonResult.result = "登入成功";
            httpSession.setAttribute("user",user);
        }
        return commonResult;
    }
}