package com.ischoolbar.programmer.controller;

import com.github.pagehelper.util.StringUtil;
import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.page.Page;
import com.ischoolbar.programmer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user")
@Controller
public class UserController {

    /*
    注意这块注解一定要加上，不写会导致这个userService不起作用，关键是系统不报错，排错也不好排，以后别犯同样的错误！
     */
    @Autowired
    public UserService userService;
    /*
    用户信息页
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model){
        model.setViewName("user/user_list");
        return model;
    }

    /**
     * 获取用户列表
     * @param username
     * @param page
     * @return
     */
    @RequestMapping(value="/get_list",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getList(
            @RequestParam(value="username",required=false,defaultValue="") String username,
            Page page
    ){
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("username", "%"+username+"%");
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        ret.put("rows", userService.findList(queryMap));
        ret.put("total", userService.getTotal(queryMap));
        return ret;
    }

    /*
    用户添加
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> add(User user){
        Map<String,String> ret=new HashMap<String, String>();
        if(user==null){
            ret.put("type","error");
            ret.put("msg","数据绑定失效，请联系开发人员");
            return ret;
        }
        if(StringUtil.isEmpty(user.getUsername())){
            ret.put("type","error");
            ret.put("msg","用户名不能为空");
            return ret;
        }
        if(StringUtil.isEmpty(user.getPassword())){
            ret.put("type","error");
            ret.put("msg","密码不能为空");
            return ret;
        }
        if(userService.findByUserName(user.getUsername())!=null){
            ret.put("type","error");
            ret.put("msg","该用户名已存在");
            return ret;
        }
        if(userService.add(user)<=0){
            ret.put("type","error");
            ret.put("msg","添加失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","添加成功");
        return ret;
    }

    /*
    用户修改功能
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> edit(User user){
        Map<String,String> ret=new HashMap<String,String>();
        if(user==null){
            ret.put("type","error");
            ret.put("msg","登录状态失效，请重新登录");
            return ret;
        }
        if(StringUtil.isEmpty(user.getUsername())){
            ret.put("type","error");
            ret.put("msg","用户名不能为空");
            return ret;
        }
        if(StringUtil.isEmpty(user.getPassword())){
            ret.put("type","error");
            ret.put("msg","用户密码不能为空");
            return ret;
        }
        User existUser = userService.findByUserName(user.getUsername());
        if(existUser != null){
            if(user.getId() != existUser.getId()){
                ret.put("type", "error");
                ret.put("msg", "该用户名已经存在!");
                return ret;
            }

        }
        if(userService.edit(user) <= 0){
            ret.put("type", "error");
            ret.put("msg", "修改失败!");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","修改成功");
        return ret;
    }

    /*
    用户删除功能
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> delte(
            @RequestParam(value="ids[]",required=true) Long[] ids){
        Map<String,String> ret=new HashMap<String, String>();
        if(ids == null){
            ret.put("type", "error");
            ret.put("msg", "请选择要删除的数据!");
            return ret;
        }
        String idsString = "";
        for(Long id:ids){
            idsString += id + ",";
        }
        idsString = idsString.substring(0,idsString.length()-1);
        if(userService.delete(idsString) <= 0){
            ret.put("type", "error");
            ret.put("msg", "删除失败!");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "修改成功!");
        return ret;
    }
}
