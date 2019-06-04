package com.jzz.newspeciality.java8.time.zone;

import java.time.ZoneOffset;
import java.time.zone.ZoneRules;

/**
 * 跟踪区域偏移如何变化
 * @author jzz
 * @date 2019/6/4
 */
public class ZoneRulesAPI {
    public static void main(String[] args) {
        // create
        /**
         * 获得具有固定区域规则的ZoneRules实例。
         * ZoneRules[currentStandardOffset=Z]
         */
        ZoneRules rules = ZoneRules.of(ZoneOffset.UTC);
        System.out.println(rules);
    }
}
