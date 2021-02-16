package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.db.DbAsteroid
import com.udacity.asteroidradar.network.AstronomyPictureOfTheDay

fun List<DbAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun AstronomyPictureOfTheDay.asDomainModel(): PictureOfDay {
    val mediaType = when (mediaTypeString) {
        "image" -> PictureOfDay.MediaType.IMAGE
        "video" -> PictureOfDay.MediaType.VIDEO
        else -> PictureOfDay.MediaType.UNKNOWN
    }
    return PictureOfDay(mediaType, title, url)
}