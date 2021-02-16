package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.db.getDatabase
import com.udacity.asteroidradar.network.NasaApiStatus
import com.udacity.asteroidradar.repository.ApodRepository
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.util.FormattedDates.datesFromTodayTillEndDate
import com.udacity.asteroidradar.util.FormattedDates.today
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _listStatus = MutableLiveData<NasaApiStatus>()
    val listStatus: LiveData<NasaApiStatus>
        get() = _listStatus

    private val _apodStatus = MutableLiveData<NasaApiStatus>()
    val apodStatus: LiveData<NasaApiStatus>
        get() = _apodStatus

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val apodRepository = ApodRepository()

    val asteroids = asteroidsRepository.asteroids
    val apod = apodRepository.apod

    init {
        tryRefresh(_listStatus) {
            asteroidsRepository.setFilters(datesFromTodayTillEndDate)
            if (asteroidsRepository.hasNotCurrentData()) {
                asteroidsRepository.refreshAsteroids()
            }
        }
        tryRefresh(_apodStatus) { apodRepository.refreshApod() }
    }

    private fun tryRefresh(status: MutableLiveData<NasaApiStatus>, refreshFun: suspend () -> Unit) {
        viewModelScope.launch {
            status.value = NasaApiStatus.LOADING
            try {
                refreshFun()
                status.value = NasaApiStatus.DONE
            } catch (e: Exception) {
                status.value = NasaApiStatus.ERROR
            }
        }
    }

    fun refreshDataIfError() {
        if (apodStatus.value == NasaApiStatus.ERROR)
            tryRefresh(_apodStatus) { apodRepository.refreshApod() }
        if (listStatus.value == NasaApiStatus.ERROR)
            tryRefresh(_listStatus) { asteroidsRepository.refreshAsteroids() }
    }

    fun setFilters(mainMenuItemId: Int) {
        viewModelScope.launch {
            when (mainMenuItemId) {
                R.id.show_week_asteroids_menu ->
                    asteroidsRepository.setFilters(datesFromTodayTillEndDate)
                R.id.show_today_asteroids_menu ->
                    asteroidsRepository.setFilters(today)
                R.id.show_saved_asteroids_menu ->
                    asteroidsRepository.setFilters()
                else -> {
                }
            }
        }
    }
}