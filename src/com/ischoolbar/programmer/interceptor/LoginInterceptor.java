package com.ischoolbar.programmer.interceptor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.JsonObject;
import com.ischoolbar.programmer.entity.User;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Object user= httpServletRequest.getSession().getAttribute("user");
        if(user==null){
            if("XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"))){
                Map<String,String> ret=new HashMap<String, String>();
                ret.put("type","error");
                ret.put("msg","登录状态已失效，请重新登录");
                httpServletResponse.getWriter().write(JSONObject.fromObject(ret).toString());
                return false;
            }
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/system/login");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
