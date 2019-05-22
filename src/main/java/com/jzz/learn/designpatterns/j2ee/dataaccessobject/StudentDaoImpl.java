package com.jzz.learn.designpatterns.j2ee.dataaccessobject;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据访问对象实体类
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class StudentDaoImpl implements StudentDao {

    /**
     * 模拟数据库
     */
    private List<Student> students;

    public StudentDaoImpl(){
        students = new ArrayList<>();
        Student student1 = new Student("Robert",0);
        Student student2 = new Student("John",1);
        students.add(student1);
        students.add(student2);
    }
    @Override
    public void deleteStudent(Student student) {
        students.remove(student.getRollNo());
        log.info("Student: Roll No :{} deleted from database",student.getRollNo());
    }

    @Override
    public List<Student> getAllStudents() {
        return students;
    }

    @Override
    public Student getStudent(int rollNo) {
        return students.get(rollNo);
    }

    @Override
    public void updateStudent(Student student) {
        students.get(student.getRollNo()).setName(student.getName());
        log.info("Student: Roll No :{}  updated in the database" , student.getRollNo());
    }
}
