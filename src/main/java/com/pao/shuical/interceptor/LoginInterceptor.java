package com.pao.shuical.interceptor;

import com.pao.shuical.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle (HttpServletRequest request,
                              HttpServletResponse response,
                              Object object) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (null != user) {
            //这里进行权限控制
            return true;
        } else {
            response.sendRedirect("/messagePage");
            return false;
        }

    }
}
