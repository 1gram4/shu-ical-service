package com.pao.shuical.controller;

import com.pao.shuical.domain.User;
import com.pao.shuical.service.UserService;
import com.pao.shuical.util.CommonResult;
import com.pao.shuical.util.CoursesToIcs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    @RequestMapping(value = "/logout" ,method = RequestMethod.GET)
    public CommonResult logout(HttpSession httpSession, HttpServletResponse response){
        User user = (User)httpSession.getAttribute("user");
        if (userService.logout(user)){
            commonResult.status = 0;
            commonResult.result= "注销成功";
            try{
                response.sendRedirect("/loginPage");//这里写得不好
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            commonResult.status = -1;
            commonResult.result = "注销失败";
        }
        return commonResult;
    }
    @RequestMapping(value = "/generateCourseTable")
    public CommonResult generateCourseTable(HttpSession httpSession){
        User user = (User)httpSession.getAttribute("user");
        if(CoursesToIcs.coursesToIcs(user.getUserName(),userService.findCourses(user))){
            commonResult.status = 0;
            commonResult.result = user.getUserName()+".ics";
        }
        else {
            commonResult.status = -1;
            commonResult.result = "课表生成失败";
        }
        return commonResult;
    }
}
