package com.jzz.learn.designpatterns.j2ee.mvc;

import lombok.extern.slf4j.Slf4j;

/**
 * 视图
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class StudentView {
    public void printStudentDetails(String studentName, String studentRollNo){
        log.info("Student: ");
        log.info("Name: " + studentName);
        log.info("Roll No: " + studentRollNo);
    }
}
