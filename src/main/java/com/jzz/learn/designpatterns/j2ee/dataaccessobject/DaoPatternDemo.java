package com.jzz.learn.designpatterns.j2ee.dataaccessobject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class DaoPatternDemo {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDaoImpl();

        //输出所有的学生
        for (Student student : studentDao.getAllStudents()) {
            log.info("Student: [RollNo : {},Name:{}]",student.getRollNo(),student.getName());
        }


        //更新学生
        Student student =studentDao.getAllStudents().get(0);
        student.setName("Michael");
        studentDao.updateStudent(student);

        //获取学生
        studentDao.getStudent(0);
        log.info("Student: [RollNo : {},Name:{}]",student.getRollNo(),student.getName());
    }
}
