package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    init {

        val fakeAsteroid1 = Asteroid(
            2465633,
            "465633 (2009 JR5)",
            "2021-05-02",
            20.3,
            517.6544821978,
            18.1279547379,
            45290438.518608145,
            true
        )

        val fakeAsteroid2 = Asteroid(
            3426410,
            "(2008 QV11)",
            "2021-06-02",
            21.0,
            375.0075217981,
            19.7498082025,
            38764457.153124401,
            false
        )

        val fakeData = listOf(fakeAsteroid1, fakeAsteroid2)
        _asteroids.value = fakeData
    }

}