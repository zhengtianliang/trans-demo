package com.zheng.controller;


import com.zheng.entity.User;
import com.zheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * @author: ztl
     * @date: 2022/12/28 09:37
     * @desc:
     */
    @GetMapping("/insert0")
    public void insert0(){
//        userService.insert0(); // 调用方没事务，被调用方有事务，不会回滚。
        userService.insert1();
    }

}
