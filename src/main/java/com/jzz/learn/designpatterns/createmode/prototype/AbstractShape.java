package com.jzz.learn.designpatterns.createmode.prototype;

/**
 * 抽象原型类
 * @author jzz
 * @date 2019/5/21
 */
public abstract class AbstractShape implements Cloneable {

    private String id;
    protected String type;

    abstract void draw();

    public String getType(){
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
