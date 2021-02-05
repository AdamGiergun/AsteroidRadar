package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.network.AsteroidsApi
import com.udacity.asteroidradar.network.asDomainModel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    init {
        viewModelScope.launch {
            val feed = AsteroidsApi.retrofitApiService.getAsteroids(
                application.getString(R.string.nasa_api_key),
                "2021-01-01",
                "2021-01-02"
            )
            _asteroids.value = feed.asDomainModel()
        }
    }
}