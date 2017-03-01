/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.helper;

import org.joda.time.*;

/**
 * DateHelper Created by Alexander on 28.02.2017.
 * Description:
 **/
public class DateHelper{

    private DateTime date = DateTime.now();

    public String getDateTimeTextPassed(){
        if(getSecondsPassed() < 60){
            return getYearsPassed() + " sec ago";
        } else if(getMinutesPassed() < 60){
            return getMinutesPassed() + " min ago";
        } else if(isToday()){
            return "Today at " + getTimeString();
        } else if(isYesterday()){
            return "Yesterday at " + getTimeString();
        }
        return getDateTimeString();
    }

    @Override
    public String toString() {
        return getDateTimeTextPassed();
    }

    public int getYearsPassed(){
        return Years.yearsBetween(date, DateTime.now()).getYears();
    }

    public int getMonthsPassed(){
        return Months.monthsBetween(date, DateTime.now()).getMonths();
    }

    public int getDaysPassed(){
        return Days.daysBetween(date, DateTime.now()).getDays();
    }

    public int getHoursPassed(){
        return Hours.hoursBetween(date, DateTime.now()).getHours();
    }

    public int getMinutesPassed(){
        return Minutes.minutesBetween(date, DateTime.now()).getMinutes();
    }

    public int getSecondsPassed(){
        return Seconds.secondsBetween(date, DateTime.now()).getSeconds();
    }


    public boolean isYesterday(){
        return getDaysPassedByTimeMidnight() == 1;
    }

    public boolean isToday(){
        return getDaysPassedByTimeMidnight() == 0;
    }

    public String getDateTimeString(){
        return getDateString() + " " + getTimeString();
    }

    public String getDateString(){
        return add0IfNeeded(date.getDayOfMonth()) + "." + add0IfNeeded(date.getMonthOfYear()) + "." + date.getYear();
    }

    public int getDaysPassedByTimeMidnight(){
        return Days.daysBetween(date.withTimeAtStartOfDay(), DateTime.now().withTimeAtStartOfDay()).getDays();
    }

    public String getTimeString(){
        return add0IfNeeded(date.getHourOfDay()) + ":" + add0IfNeeded(date.getMinuteOfHour());
    }

    public static String add0IfNeeded(int i){
        if(i < 0) return "00";
        if(i < 10)return "0" + i;
        return "" + i;
    }
}
