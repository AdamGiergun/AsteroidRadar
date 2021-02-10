package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.db.getDatabase
import com.udacity.asteroidradar.network.NasaApiStatus
import com.udacity.asteroidradar.repository.ApodRepository
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _asteroidsStatus = MutableLiveData<NasaApiStatus>()
    val asteroidsStatus: LiveData<NasaApiStatus>
        get() = _asteroidsStatus

    private val _apodStatus = MutableLiveData<NasaApiStatus>()
    val apodStatus: LiveData<NasaApiStatus>
        get() = _apodStatus

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val apodRepository = ApodRepository()

    init {
        viewModelScope.launch {
            _asteroidsStatus.value = NasaApiStatus.LOADING
            try {
                if (asteroidsRepository.hasNoFreshData()) {
                    asteroidsRepository.refreshAsteroids()
                }
                _asteroidsStatus.value = NasaApiStatus.DONE
            } catch (e: Exception) {
                _asteroidsStatus.value = NasaApiStatus.ERROR
            }
        }

        viewModelScope.launch {
            _apodStatus.value = NasaApiStatus.LOADING
            try {
                apodRepository.refreshApod()
                _apodStatus.value = NasaApiStatus.DONE
            } catch (e: Exception) {
                _apodStatus.value = NasaApiStatus.ERROR
            }
        }
    }

    val asteroids = asteroidsRepository.asteroids
    val apod = apodRepository.apod
}