package com.zheng.service.impl;

import com.zheng.entity.User;
import com.zheng.mapper.UserMapper;
import com.zheng.service.TeacherService;
import com.zheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: ztl
 * @date: 2022/12/27 11:18
 * @desc:
 */
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
     * @desc: 测试：外部无事务，内部有事务，内部事务是required.外部抛异常。
     *  结论：外部、内部都不会回滚。
     *  测试User(A)没有事务，teacher(B)会自己创建事务。
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

    /**
     * @author: ztl
     * @date: 2022/12/28 10:20
     * @desc: 测试：外部有事务，是required，内部有事务，是required。外部抛异常。
     *   结论：外部、内部都会回滚事务。
     *   A(调用方，也就是insert1)方法加了@Transactional注解，会开启事务。
     *   teacherService.insert(); 也加了@Transactional注解，也会开启事务
     *   由于user的insert1方法有事务了，而且teacherService.insert()的类型是REQUIRED，
     *          所以teacher的事务会加入到user的事务(insert1方法的事务)，就是同一个事务了。
     *   同一个事务中，有异常，所以两个都会回滚
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void insert1(){
        teacherService.insert();

        User user = new User(2,"user_"+System.currentTimeMillis());
        userMapper.insert(user);

        int i = 1/0; // 这里抛出异常，两个提交操作都会回滚(不会成功)
    }

    /**
     * @author: ztl
     * @date: 2022/12/27 11:18
     * @desc: 测试：外部有事务，是required，内部有事务，是required_new。外部抛异常
     *  结论：外层insert2报错，teacherService.insert2不会回滚，userMapper.insert会回滚。
     *   teacherService.insert2()的传播行为是propagation = Propagation.REQUIRES_NEW
     *   由于调用方(A,也就是insert2)加了Transactional，开启了事务，被调用方(B,也就是teacherService.insert2)也开启了事务，
     *          并且B的传播行为是REQUIRED_NEW，不管当前有没有事务，都会新建一个事务，并且外部事务回滚不会影响内部事务提交。
     *          所以teacherService.insert2()新建了一个新事务，并且新事务成功，所以会提交。
     *          即使外部user的insert2方法异常了，也不影响内部teacher的insert2方法的提交
     *   总的流程如下：
     *      user的insert2开启了一个事务。
     *      teacher的insert2开启了一个事务，并且将user的insert2的事务挂起
     *      user的insert2的事务运行完毕，再恢复user的insert2的事务，
     *      两个事务的提交和回滚都在各自的事务中完成。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void insert2(){
        teacherService.insert2();

        User user = new User(1,"user_"+System.currentTimeMillis());
        userMapper.insert(user);

        int i = 1/0; // 这里抛出异常，两个提交操作都会回滚(不会成功)
    }

    /**
     * @author: ztl
     * @date: 2022/12/27 11:18
     * @desc: 测试：外部有事务，是required。内部有事务，是required_new，在内部抛出异常。
     *  结论：外部、内部都会回滚。虽然是俩事务，但是内部事务异常回滚了，内部会将异常抛出去，相当于外部也有异常了。
     *  外部有事务，是required，内部有事务，是required_new.
     *  内部事务开启，将外部事务挂起，内部事务异常，内部事务回滚。并且，内部将异常抛出到外部，
     *  内部事务执行完毕，将外部事务从挂起恢复，外部事务异常(是由内部抛出来的)，外部事务回滚。
     *  其实teacherService.insert3()异常，就相当于直接在这行代码下面，写一句：
     *      throw new Exception..，所以，内部、外部都会回滚。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void insert3(){

        User user = new User(1,"user_"+System.currentTimeMillis());
        userMapper.insert(user);

        // 这个方法会抛出异常，就相当于直接在这个代码的下面写： int i = 1/0,也就相当于直接在这段下面 throw new Exception..
        teacherService.insert3();
    }

}
