package com.zheng.mapper;

import com.zheng.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> selectList();


    void insert(User user);
}
