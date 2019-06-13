package com.jzz.newspeciality.java8.time.temporal.temporalamount;

import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;

public class ChronoPeriodAPI {
    public static void main(String[] args) {
        ChronoPeriod.between(ChronoLocalDate.from(LocalDate.now()),ChronoLocalDate.from(LocalDate.now().plusDays(1)));


        ChronoPeriod chronoPeriod = Period.ofDays(1);
        ChronoPeriod between = ChronoPeriod.between(ChronoLocalDate.from(LocalDate.now()), ChronoLocalDate.from(LocalDate.now().plusDays(1)));
        System.out.println(between);
    }
}
