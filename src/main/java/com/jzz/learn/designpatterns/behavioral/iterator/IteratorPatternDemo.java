package com.jzz.learn.designpatterns.behavioral.iterator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IteratorPatternDemo {
    public static void main(String[] args) {
        NameRepository namesRepository = new NameRepository();
        for(Iterator iter = namesRepository.getIterator(); iter.hasNext();){
            String name = (String)iter.next();
            log.info("Name:{}",name);
        }
    }
}
