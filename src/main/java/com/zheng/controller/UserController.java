package com.zheng.controller;


import com.zheng.entity.User;
import com.zheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/test")
    public List<User> list(){
        return userService.list();
    }
}
