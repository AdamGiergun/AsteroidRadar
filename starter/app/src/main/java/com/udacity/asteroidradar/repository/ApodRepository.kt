package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.NASA_API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.TODAY_DATE_FORMATTED
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.util.asDomainModel

class ApodRepository {

    private val _apod = MutableLiveData<PictureOfDay>()
    val apod: LiveData<PictureOfDay>
        get() = _apod

    suspend fun refreshApod() {
        _apod.value = NasaApi.retrofitApiService.getAPOD(
            NASA_API_KEY,
            TODAY_DATE_FORMATTED
            // "2021-02-09" // use it for testing PictureOfDay.MediaType.VIDEO
        ).asDomainModel()
    }
}