package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat
import java.util.*

object FormattedDates {
    val todayFormattedDate: List<String>
        get() {
            val calendar = Calendar.getInstance()
            return listOf(formattedDate(calendar))
        }

    val todayAndNextSevenDaysFormattedDates: List<String>
        get() {
            val calendar = Calendar.getInstance()
            val list = mutableListOf(formattedDate(calendar))

            repeat(Constants.DEFAULT_END_DATE_DAYS) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                list.add(formattedDate(calendar))
            }
            return list
        }

    private fun formattedDate(day: Calendar): String {
        return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            .format(day.time)
    }
}