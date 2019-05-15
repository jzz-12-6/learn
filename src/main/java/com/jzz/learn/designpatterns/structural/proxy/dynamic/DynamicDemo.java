package com.jzz.learn.designpatterns.structural.proxy.dynamic;

import com.jzz.learn.designpatterns.structural.proxy.staticstate.Image;
import com.jzz.learn.designpatterns.structural.proxy.staticstate.RealImage;

import java.lang.reflect.Proxy;

/**
 * 动态代理测试
 */
public class DynamicDemo {
    public static void main(String[] args) {
        RealImage realImage  = new RealImage("this");
        //生成代理对象使用哪个类装载器
        ClassLoader classLoader = realImage.getClass().getClassLoader();
        //生成哪个对象的代理对象，通过接口指定【指定要被代理类的接口】
        Class<?>[] classes = realImage.getClass().getInterfaces();
        Image  image = (Image)Proxy.newProxyInstance(classLoader,classes,((proxy, method, args1) -> {
            if(method.getName().equals("display")){
                method.invoke(realImage,args1);
            }else {
                return method.invoke(realImage,args1);
            }
            return null;
        }));
        image.display();
    }
}
