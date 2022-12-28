package com.zheng.service.impl;

import com.zheng.entity.User;
import com.zheng.mapper.UserMapper;
import com.zheng.service.TeacherService;
import com.zheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeacherService teacherService;

    @Override
    public List<User> list() {
        return userMapper.selectList();
    }

    /**
     * @author: ztl
     * @date: 2022/12/27 11:18
     * @desc: 测试User(A)没有事务，teacher(B)会自己创建事务。
     *  这里没有开启事务，teacherService.insert(); 会自己开启事务。
     *          teacherService.insert()方法上有@Transactional(propagation = Propagation.REQUIRED)
     *  而且俩个添加操作都会成功。因为userMapper.insert(user);是自动提交的(因为这个insert0方法没开启事务，又是mysql，所以自动提交了)
     *
     *      因为调用方的A方法(insert0)没有事务，所以 userMapper.insert(user);会自动提交，因为没有事务，且是mysql默认级别是可重复读，自动提交
     *      而teacherService.insert();虽然加了注解，开启了事务，但是由于他成功了，所以他也提交了。
     *      所以尽管insert0方法最后报错了，但是由于这俩都各自提交了，所以并不会回滚。
     */
    @Override
    public void insert0(){

        teacherService.insert();

        User user = new User(1,"user_"+System.currentTimeMillis());
        userMapper.insert(user);

        int i = 1/0; // 这里抛出异常，两个提交操作都不会回滚(会成功)
    }

}
