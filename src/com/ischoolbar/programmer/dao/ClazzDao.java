package com.ischoolbar.programmer.dao;

import com.ischoolbar.programmer.entity.Clazz;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@ResponseBody
public interface ClazzDao {
    public int add(Clazz clazz);
    public int edit(Clazz clazz);
    public int delete(String ids);
    public List<Clazz> findList(Map<String,Object> queryMap);
    public List<Clazz> findAll();
    public int getTotal(Map<String,Object> queryMap);
}
