package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NASA_API_KEY
import com.udacity.asteroidradar.TODAY_DATE_FORMATTED
import com.udacity.asteroidradar.db.AsteroidsDb
import com.udacity.asteroidradar.db.AsteroidDateFilter
import com.udacity.asteroidradar.db.asDomainModel
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.asDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val db: AsteroidsDb) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(db.asteroidDao.getAsteroids()) {
            it.asDomainModel()
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

    fun hasNoFreshData(): Boolean {
        asteroids.value.let { list ->
            return list == null || list.isEmpty()
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
