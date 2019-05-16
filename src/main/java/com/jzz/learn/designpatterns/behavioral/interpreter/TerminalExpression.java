package com.jzz.learn.designpatterns.behavioral.interpreter;
/**
 * 终结符表达式类
 * @author jzz
 * @date 2019-5-16
 */
public class TerminalExpression implements Expression {

    private String data;

    public TerminalExpression(String data){
        this.data = data;
    }

    @Override
    public boolean interpret(String context) {
        return context.contains(data);
    }
}
