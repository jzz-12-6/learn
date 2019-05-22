package com.jzz.learn.designpatterns.j2ee.transferobject;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务对象
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class StudentBO {
    /**
     * 列表是当作一个数据库
     */
    private   List<StudentVO> students;

    public StudentBO(){
        students = new ArrayList<>();
        StudentVO student1 = new StudentVO("Robert",0);
        StudentVO student2 = new StudentVO("John",1);
        students.add(student1);
        students.add(student2);
    }
    public void deleteStudent(StudentVO student) {
        students.remove(student.getRollNo());
        log.info("Student: Roll No :{} deleted from database",student.getRollNo());
    }

    //从数据库中检索学生名单
    public List<StudentVO> getAllStudents() {
        return students;
    }

    public StudentVO getStudent(int rollNo) {
        return students.get(rollNo);
    }

    public void updateStudent(StudentVO student) {
        students.get(student.getRollNo()).setName(student.getName());
        log.info("Student: Roll No :{} updated from database",student.getRollNo());
    }
}
