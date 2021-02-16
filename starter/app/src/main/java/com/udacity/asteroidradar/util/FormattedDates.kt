package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.util.Constants.DEFAULT_END_DATE_DAYS
import java.text.SimpleDateFormat
import java.util.*

object FormattedDates {
    val today: List<String>
        get() {
            val calendar = Calendar.getInstance()
            return listOf(formattedDate(calendar))
        }

    val datesFromTodayTillEndDate: List<String>
        get() {
            val calendar = Calendar.getInstance()
            val list = mutableListOf(formattedDate(calendar))

            repeat(DEFAULT_END_DATE_DAYS) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                list.add(formattedDate(calendar))
            }
            return list
        }

    val currentEndDate: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, DEFAULT_END_DATE_DAYS)
            return formattedDate(calendar)
        }

    private fun formattedDate(day: Calendar): String {
        return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            .format(day.time)
    }
}