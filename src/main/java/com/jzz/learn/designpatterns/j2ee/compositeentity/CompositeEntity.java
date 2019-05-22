package com.jzz.learn.designpatterns.j2ee.compositeentity;

/**
 * 组合实体
 * @author jzz
 * @date 2019/5/22
 */
public class CompositeEntity {
    private CoarseGrainedObject cgo = new CoarseGrainedObject();

    public void setData(String data1, String data2){
        cgo.setData(data1, data2);
    }

    public String[] getData(){
        return cgo.getData();
    }
}
