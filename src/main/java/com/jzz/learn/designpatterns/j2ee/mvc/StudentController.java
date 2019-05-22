package com.jzz.learn.designpatterns.j2ee.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 控制层
 * @author jzz
 * @date 2019/5/22
 */
@Data
@AllArgsConstructor
public class StudentController {
    private Student model;
    private StudentView view;


    public void setStudentName(String name){
        model.setName(name);
    }

    public String getStudentName(){
        return model.getName();
    }

    public void setStudentRollNo(String rollNo){
        model.setRollNo(rollNo);
    }

    public String getStudentRollNo(){
        return model.getRollNo();
    }

    public void updateView(){
        view.printStudentDetails(model.getName(), model.getRollNo());
    }
}
