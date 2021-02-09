package com.udacity.asteroidradar

import android.app.Application
import java.text.SimpleDateFormat
import java.util.*

lateinit var NASA_API_KEY: String
lateinit var TODAY_DATE_FORMATTED: String

class AsteroidRadarApp: Application() {

    override fun onCreate() {
        super.onCreate()
        NASA_API_KEY = getString(R.string.nasa_api_key)
        TODAY_DATE_FORMATTED = todayDateFormatted
    }

    private val todayDateFormatted = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        .format(Calendar.getInstance().time)
}