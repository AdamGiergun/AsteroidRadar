package com.udacity.asteroidradar

import android.app.Application

lateinit var NASA_API_KEY: String

class AsteroidRadarApp: Application() {

    override fun onCreate() {
        super.onCreate()
        NASA_API_KEY = getString(R.string.nasa_api_key)
    }
}