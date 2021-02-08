package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.db.getDatabase
import com.udacity.asteroidradar.network.AsteroidsApiStatus
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _status = MutableLiveData<AsteroidsApiStatus>()
    val status: LiveData<AsteroidsApiStatus>
        get() = _status

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    init {
        viewModelScope.launch {
            _status.value = AsteroidsApiStatus.LOADING
            try {
                asteroidsRepository.refreshAsteroids()
                _status.value = AsteroidsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidsApiStatus.ERROR
            }
        }
    }

    val asteroids = asteroidsRepository.asteroids
}