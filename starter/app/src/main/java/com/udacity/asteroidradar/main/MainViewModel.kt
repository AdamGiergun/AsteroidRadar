package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.network.AsteroidsApi
import com.udacity.asteroidradar.network.AsteroidsApiStatus
import com.udacity.asteroidradar.network.asDomainModel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _status = MutableLiveData<AsteroidsApiStatus>()
    val status: LiveData<AsteroidsApiStatus>
        get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    init {
        viewModelScope.launch {
            _status.value = AsteroidsApiStatus.LOADING
            try {
                _asteroids.value = AsteroidsApi.retrofitApiService.getAsteroids(
                    application.getString(R.string.nasa_api_key),
                    "2021-01-01",
                    "2021-01-02"
                ).asDomainModel()
                _status.value = AsteroidsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidsApiStatus.ERROR
                _asteroids.value = emptyList()
            }
        }
    }
}