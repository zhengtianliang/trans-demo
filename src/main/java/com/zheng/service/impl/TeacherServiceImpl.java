package com.zheng.service.impl;

import com.zheng.entity.Teacher;
import com.zheng.mapper.TeacherMapper;
import com.zheng.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ztl
 * @date: 2022/12/27 11:18
 * @desc:
 */

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert() {
        Teacher teacher = new Teacher(1,"teacher_"+System.currentTimeMillis());
        return teacherMapper.insert(teacher);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertWithIdWithTrans(Integer id) {
        Teacher teacher = new Teacher(id,"teacher_"+System.currentTimeMillis());
        return teacherMapper.insert(teacher);
    }

    // 测试 propagation = Propagation.REQUIRES_NEW
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insert2() {
        Teacher teacher = new Teacher(1,"teacher_"+System.currentTimeMillis());
        return teacherMapper.insert(teacher);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public int insert3() {
        Teacher teacher = new Teacher(1,"teacher_"+System.currentTimeMillis());
        int insert = teacherMapper.insert(teacher);

        int i = 1/0; // 这里抛出异常，内部有异常的话，会将异常抛出去，所以外部也会有异常了。
        return insert;
    }
}
