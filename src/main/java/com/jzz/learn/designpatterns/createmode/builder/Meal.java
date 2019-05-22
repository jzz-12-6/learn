package com.jzz.learn.designpatterns.createmode.builder;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品角色
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public  class Meal {
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item){
        items.add(item);
    }

    public float getCost(){
        return items.stream().map(Item::price).reduce(0f,Float::sum);
    }

    public void showItems(){
        for (Item item : items) {
            log.info("Item : "+item.name());
            log.info(", Packing : "+item.packing().pack());
            log.info(", Price : "+item.price());
        }
    }
}
