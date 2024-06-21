package com.example.calendarapp.utils

import org.joda.time.DateTime
import java.util.Date


fun getMonth(month : Int) : String {
    return when (month) {
        1 -> "JAN"
        2 -> "FEB"
        3 -> "MAR"
        4 -> "APR"
        5 -> "MAY"
        6 -> "JUN"
        7 -> "JUL"
        8 -> "AUG"
        9 -> "SEP"
        10 -> "OCT"
        11 -> "NOV"
        12 -> "DEC"
        else -> {""}
    }
}

fun createDate(day : Int, monthofYear : Int, year : Int) : DateTime {
    return DateTime().withYear(year).withMonthOfYear(monthofYear).withDayOfMonth(day)
        .withTime(0, 0, 0, 0)
}

fun isToday(date: DateTime): Boolean {
    val dtToCompare = date.withTime(0,0, 0, 0)
    val dtToday = DateTime(Date()).withTime(0, 0, 0, 0)
    return dtToCompare.isEqual(dtToday)
}