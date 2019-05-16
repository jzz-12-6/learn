package com.jzz.learn.designpatterns.behavioral.interpreter;
/**
 * 环境类
 * @author jzz
 * @date 2019-5-16
 */
public class Context {

    /**
     * 满足两个规则
     * @param ex1 规则1
     * @param ex2 规则2
     * @return Expression
     */
    public static Expression getMaleExpression(Expression ex1,Expression ex2){
        return new OrExpression(ex1, ex2);
    }

    /**
     * 满足两个规则
     * @param ex1 规则1
     * @param ex2 规则2
     * @return Expression
     */
    public static Expression getMarriedWomanExpression(Expression ex1,Expression ex2){
        return new AndExpression(ex1, ex2);
    }
}
