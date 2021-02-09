package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.NASA_API_KEY
import com.udacity.asteroidradar.network.AstronomyPictureOfTheDay
import com.udacity.asteroidradar.network.NasaApi

class ApodRepository {

    private val _apod = MutableLiveData<AstronomyPictureOfTheDay>()
    val apod: LiveData<AstronomyPictureOfTheDay>
        get() = _apod

    suspend fun refreshApod() {
        _apod.value = NasaApi.retrofitApiService.getAPOD(
            NASA_API_KEY,
            "2021-02-08"
        )
    }
}