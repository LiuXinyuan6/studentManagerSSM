package com.ischoolbar.programmer.service;

import com.ischoolbar.programmer.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    public User findByUserName(String username);
    public int add(User user);
    public List<User> findList(Map<String,Object> queryMap);
    public int getTotal(Map<String,Object> queryMap);
    public int edit(User user);
    public int delete(String ids);
}
