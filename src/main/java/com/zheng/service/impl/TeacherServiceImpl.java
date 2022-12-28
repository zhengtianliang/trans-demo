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
}
