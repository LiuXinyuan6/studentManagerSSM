package com.ischoolbar.programmer.controller;


import com.github.pagehelper.util.StringUtil;
import com.ischoolbar.programmer.entity.Student;
import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.service.StudentService;
import com.ischoolbar.programmer.service.UserService;
import com.ischoolbar.programmer.util.CpachaUtil;
import com.mysql.cj.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.swing.StringUIClientPropertyKey;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/system")
@Controller
public class SystemController{
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @RequestMapping(value = "/index",method=RequestMethod.GET)
    public ModelAndView index(ModelAndView model){
        model.setViewName("system/index");
        return model;
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET )
    public ModelAndView login(ModelAndView model){
        model.setViewName("system/login");
        return model;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST )
    @ResponseBody
    public Map<String,String> login(
        @RequestParam(value = "username",required = true) String username,
        @RequestParam(value = "password",required = true) String password,
        @RequestParam(value = "vcode",required = true) String vcode,
        @RequestParam(value="type",required=true) int type,
        HttpServletRequest request
        ){
        Map<String,String> ret=new HashMap<String,String>();
        if(StringUtil.isEmpty(username)){
            ret.put("type","error");
            ret.put("msg","用户名不能为空");
            return ret;
        }
        if(StringUtil.isEmpty(password)){
            ret.put("type","error");
            ret.put("msg","密码不能为空");
            return ret;
        }
        if(StringUtil.isEmpty(vcode)){
            ret.put("type","error");
            ret.put("msg","验证码不能为空");
            return ret;
        }
        String Cpacha=(String)request.getSession().getAttribute("loginCpacha");
        if(StringUtil.isEmpty(Cpacha)){
            ret.put("type","error");
            ret.put("msg","长时间未操作，会话已结束，请重新刷新页面");
            return ret;
        }
        if(!Cpacha.equalsIgnoreCase(vcode)){
            ret.put("type","error");
            ret.put("msg","验证码错误");
            return ret;
        }
        request.getSession().setAttribute("loginCpacha",null);
        if(type==1){
            //管理员
            if(userService.findByUserName(username)==null){
                ret.put("type","error");
                ret.put("msg","用户不存在");
                return ret;
            }
            if(!password.equalsIgnoreCase(userService.findByUserName(username).getPassword())){
                ret.put("type","error");
                ret.put("msg","用户密码不正确");
                return ret;
            }
            request.getSession().setAttribute("user",userService.findByUserName(username));
        }
        if(type==2){
            //学生
            if(studentService.findByUserName(username)== null){
                ret.put("type", "error");
                ret.put("msg", "不存在该学生!");
                return ret;
            }
            if(!password.equals(studentService.findByUserName(username).getPassword())){
                ret.put("type", "error");
                ret.put("msg", "密码错误!");
                return ret;
            }
            request.getSession().setAttribute("user", studentService.findByUserName(username));
        }
        request.getSession().setAttribute("userType",type);
        ret.put("type","success");
        ret.put("msg","登陆成功");
        return ret;
    }


    @RequestMapping(value="/get_cpacha",method = RequestMethod.GET)
    public void getCpacha(HttpServletRequest request,
                          @RequestParam(value = "vl",defaultValue = "4",required = false) Integer vl,
                          @RequestParam(value = "w",defaultValue = "98",required = false) Integer w,
                          @RequestParam(value = "h",defaultValue = "33",required = false) Integer h,
                          HttpServletResponse response){
        CpachaUtil cpachaUtil=new CpachaUtil(vl,w,h);
        String generatorVCode=cpachaUtil.generatorVCode();
        request.getSession().setAttribute("loginCpacha",generatorVCode);
        BufferedImage generatorRotateVCodeImage=cpachaUtil.generatorRotateVCodeImage(generatorVCode,true);
        try {
            ImageIO.write(generatorRotateVCodeImage,"gif",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
