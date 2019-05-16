package com.jzz.learn.designpatterns.behavioral.interpreter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterpreterPatternDemo {

    public static void main(String[] args) {
        Expression robert = new TerminalExpression("Robert");
        Expression john = new TerminalExpression("John");
        Expression julie = new TerminalExpression("Julie");
        Expression married = new TerminalExpression("Married");
        Expression isMale = Context.getMaleExpression(robert, john);
        Expression isMarriedWoman =  Context.getMarriedWomanExpression(julie, married);
        log.info("John is male? :{}",isMale.interpret("John"));
        log.info("Julie is a married women? :{}",isMarriedWoman.interpret("Married Julie"));
    }
}
