package com.jzz.learn.designpatterns.j2ee.transferobject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class TransferObjectPatternDemo {
    public static void main(String[] args) {
        StudentBO studentBusinessObject = new StudentBO();

        //输出所有的学生
        for (StudentVO student : studentBusinessObject.getAllStudents()) {
            log.info("Student: [RollNo : "
                    +student.getRollNo()+", Name : "+student.getName()+" ]");
        }

        //更新学生
        StudentVO student =studentBusinessObject.getAllStudents().get(0);
        student.setName("Michael");
        studentBusinessObject.updateStudent(student);

        //获取学生
        studentBusinessObject.getStudent(0);
        log.info("Student: [RollNo : "
                +student.getRollNo()+", Name : "+student.getName()+" ]");
    }
}
