package com.jzz.learn.designpatterns.behavioral.template;

/**
 * @author jzz
 * @date 2019/5/20
 */
public class TemplatePatternDemo {
    public static void main(String[] args) {

        AbstractGame game = new Cricket();
        game.play();

        game = new Football();
        game.play();
    }
}
