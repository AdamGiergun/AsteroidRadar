package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.db.getDatabase
import com.udacity.asteroidradar.network.NasaApiStatus
import com.udacity.asteroidradar.repository.ApodRepository
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
            asteroidsRepository.setFilters(todayAndNextSevenDays)
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

    fun setFilters(mainMenuItemId: Int) {
        viewModelScope.launch {
            when (mainMenuItemId) {
                R.id.show_week_asteroids_menu ->
                    asteroidsRepository.setFilters(todayAndNextSevenDays)
                R.id.show_today_asteroids_menu ->
                    asteroidsRepository.setFilters(today)
                R.id.show_saved_asteroids_menu ->
                    asteroidsRepository.setFilters()
                else -> {
                }
            }
        }
    }

    private val today: List<String>
        get() {
            val today = Calendar.getInstance()
            return listOf(formatDate(today))
        }

    private val todayAndNextSevenDays: List<String>
        get() {
            val day = Calendar.getInstance()
            val list = mutableListOf(formatDate(day))

            repeat(7) {
                day.add(Calendar.DAY_OF_MONTH, 1)
                list.add(formatDate(day))
            }
            return list
        }

    private fun formatDate(day: Calendar): String {
        return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            .format(day.time)
    }
}