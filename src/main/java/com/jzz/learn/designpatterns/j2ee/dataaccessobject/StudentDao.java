package com.jzz.learn.designpatterns.j2ee.dataaccessobject;

import java.util.List;

/**
 * 数据访问对象接口
 * @author jzz
 * @date 2019/5/22
 */
public interface StudentDao {
     List<Student> getAllStudents();
     Student getStudent(int rollNo);
     void updateStudent(Student student);
     void deleteStudent(Student student);
}
