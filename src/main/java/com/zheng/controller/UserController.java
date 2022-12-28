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
//        userService.insert0(); // 外部无事务，内部有事务required，外部抛出异常，内部、外部都不会回滚。
//        userService.insert1(); // 外部有事务，required，内部有事务，required，外部抛出异常，俩个都会回滚
//        userService.insert2(); // 外部required，内部required_new，外部抛出异常，内部事务提交，外部事务回滚
        userService.insert3(); // 外部required，内部required_new，内部抛出异常，两个都回滚
    }

}
