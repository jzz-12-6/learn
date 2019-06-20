package com.jzz.newspeciality.java8.time.temporal;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.*;

/**
 *
 * 定义对时态对象的只读访问的框架级接口，
 * 例如日期，时间，偏移或这些的基本接口类型
 * @author jzz
 * @date 2019/6/6
 */
public class TemporalAccessorAPI {

    public static void main(String[] args) {
        /**
         *
         */
        TemporalAccessor t = LocalDate.now();
        /**
         * 是否支持指定字段
         */
        boolean supported = t.isSupported(ChronoField.DAY_OF_YEAR);
        /**
         * 获取指定字段的范围
         */
        ValueRange range = t.range(ChronoField.DAY_OF_YEAR);
        /**
         * 获取指定范围的值
         */
        int i = t.get(ChronoField.DAY_OF_YEAR);
        t.getLong(ChronoField.DAY_OF_YEAR);
        /**
         * 将时间转为其他格式
         */
        LocalDate query = t.query(LocalDate::from);
        ZoneId query1 = t.query(TemporalQueries.zoneId());
    }
}
