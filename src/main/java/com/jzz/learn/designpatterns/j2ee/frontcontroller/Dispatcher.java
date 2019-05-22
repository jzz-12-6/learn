package com.jzz.learn.designpatterns.j2ee.frontcontroller;

/**
 * 调度器
 * @author jzz
 * @date 2019/5/22
 */
public class Dispatcher {
    private StudentView studentView;
    private HomeView homeView;
    public Dispatcher(){
        studentView = new StudentView();
        homeView = new HomeView();
    }

    public void dispatch(String request){
        if(request.equalsIgnoreCase("STUDENT")){
            studentView.show();
        }else{
            homeView.show();
        }
    }
}
