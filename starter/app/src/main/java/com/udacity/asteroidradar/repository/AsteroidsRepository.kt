package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.NASA_API_KEY
import com.udacity.asteroidradar.db.AsteroidsDb
import com.udacity.asteroidradar.db.asDomainModel
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.asDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val db: AsteroidsDb) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(db.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = NasaApi.retrofitApiService.getAsteroids(
                NASA_API_KEY,
                "2021-02-01",
                "2021-02-02"
            ).asDbModel()
            db.asteroidDao.insertAll(*asteroids)
        }
    }
}