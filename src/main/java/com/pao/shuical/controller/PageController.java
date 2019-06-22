package com.pao.shuical.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {
    @RequestMapping(value = "/loginPage" ,method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }
    @RequestMapping(value = "/indexPage",method = RequestMethod.GET)
    public String indexPage(){
        return"index";
    }
    @RequestMapping(value = "/messagePage",method = RequestMethod.GET)
    public String messagePage(){return "message";}
    @RequestMapping(value = "/grabCoursePage",method = RequestMethod.GET)
    public String grabCoursePage(){
        return "grabCourse";
    }
    @RequestMapping(value = "/contactAuthorPage",method = RequestMethod.GET)
    public String contactAuthorPage(){
        return "contactAuthor";
    }
}
