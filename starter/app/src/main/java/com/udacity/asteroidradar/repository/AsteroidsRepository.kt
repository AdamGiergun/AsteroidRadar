package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NASA_API_KEY
import com.udacity.asteroidradar.TODAY_DATE_FORMATTED
import com.udacity.asteroidradar.db.AsteroidsDb
import com.udacity.asteroidradar.db.AsteroidDateFilter
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.asDbModel
import com.udacity.asteroidradar.util.FormattedDates.currentEndDate
import com.udacity.asteroidradar.util.FormattedDates.today
import com.udacity.asteroidradar.util.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val db: AsteroidsDb) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(db.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun deleteOldAsteroids() {
        db.asteroidDao.deleteOldAsteroids(today)
    }

    suspend fun hasNotCurrentData(): Boolean {
        return db.asteroidDao.getMaxDate() != currentEndDate
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = NasaApi.retrofitApiService.getAsteroids(
                NASA_API_KEY,
                TODAY_DATE_FORMATTED
            ).asDbModel()
            db.asteroidDao.insertAsteroids(*asteroids)
        }
    }

    suspend fun setFilters(dates: List<String> = emptyList()) {
        db.asteroidDao.deleteAllDates()
        val asteroidDates =
            if (dates.isEmpty())
                getAllDates()
            else
                dates.map { AsteroidDateFilter(it) }
        db.asteroidDao.insertDates(*asteroidDates.toTypedArray())
    }

    private suspend fun getAllDates() = db.asteroidDao.getAllDates().map { AsteroidDateFilter(it) }
}