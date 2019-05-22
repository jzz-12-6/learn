package com.jzz.learn.designpatterns.j2ee.compositeentity;

/**
 * 粗粒度对象
 * @author jzz
 * @date 2019/5/22
 */
public class CoarseGrainedObject {
    DependentObject1 do1 = new DependentObject1();
    DependentObject2 do2 = new DependentObject2();

    public void setData(String data1, String data2){
        do1.setData(data1);
        do2.setData(data2);
    }

    public String[] getData(){
        return new String[] {do1.getData(),do2.getData()};
    }
}
