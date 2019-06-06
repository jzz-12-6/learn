package com.jzz.newspeciality.java8.time.temporal;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

/**
 * 斜杠
 * 定义对时态对象的只读访问的框架级接口，
 * 例如日期，时间，偏移或这些的某种组合。
 * @author jzz lastname
 * @date 2019/6/6
 */
public class TemporalAccessorAPI {

    public static void main(String[] args) {
        /**
         *
         */
        TemporalAccessor t = LocalDate.now();
        String imgUrl = "https://im-headimg-hongkong.oss-cn-hongkong.aliyuncs.com/f-1559734941738NQLKA8BB.jpg?x-oss-process=image/resize,h_400";
        int index = imgUrl.indexOf('?');
        if(index != -1){
            imgUrl = imgUrl.substring(0,index);
        }
        int x = imgUrl.lastIndexOf('/');
        String fileId = imgUrl.substring(x+1);
        System.out.println(fileId);
    }
}
