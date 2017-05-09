/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.helper

import org.joda.time.*

/**
 * DateHelper Created by Alexander on 28.02.2017.
 * Description:
 */
class DateHelper {

    var date = DateTime.now()

    constructor()

    constructor(time : String){
        date = DateTime.parse(time)
    }

    val dateTimeTextPassed: String
        get() {
            if (secondsPassed < 60) {
                return yearsPassed.toString() + " sec ago"
            } else if (minutesPassed < 60) {
                return minutesPassed.toString() + " min ago"
            } else if (isToday) {
                return "Today at " + timeString
            } else if (isYesterday) {
                return "Yesterday at " + timeString
            }
            return dateTimeString
        }

    override fun toString(): String {
        return dateTimeTextPassed
    }

    val yearsPassed: Int
        get() = Years.yearsBetween(date, DateTime.now()).years

    val monthsPassed: Int
        get() = Months.monthsBetween(date, DateTime.now()).months

    val daysPassed: Int
        get() = Days.daysBetween(date, DateTime.now()).days

    val hoursPassed: Int
        get() = Hours.hoursBetween(date, DateTime.now()).hours

    val minutesPassed: Int
        get() = Minutes.minutesBetween(date, DateTime.now()).minutes

    val secondsPassed: Int
        get() = Seconds.secondsBetween(date, DateTime.now()).seconds


    val isYesterday: Boolean
        get() = daysPassedByTimeMidnight == 1

    val isToday: Boolean
        get() = daysPassedByTimeMidnight == 0

    val dateTimeString: String
        get() = dateString + " " + timeString

    val dateString: String
        get() = add0IfNeeded(date.dayOfMonth) + "." + add0IfNeeded(date.monthOfYear) + "." + date.year

    val daysPassedByTimeMidnight: Int
        get() = Days.daysBetween(date.withTimeAtStartOfDay(), DateTime.now().withTimeAtStartOfDay()).days

    val timeString: String
        get() = add0IfNeeded(date.hourOfDay) + ":" + add0IfNeeded(date.minuteOfHour)

    companion object {

        fun add0IfNeeded(i: Int): String {
            if (i < 0) return "00"
            if (i < 10) return "0" + i
            return "" + i
        }
    }
}
