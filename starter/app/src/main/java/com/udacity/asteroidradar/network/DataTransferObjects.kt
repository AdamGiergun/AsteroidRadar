/**
 * Big thanks to Filipe Bezerra for getting the idea of parsing nested JSON objects with Moshi
 * https://github.com/filipebezerra/near-earth-asteroids-app-android-kotlin
 * /blob/main/app/src/main/java/dev/filipebezerra/android/nearearthasteroids/datasource/remote/DataTransferObjects.kt
 */

package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.db.DbAsteroid

@JsonClass(generateAdapter = true)
data class NearEarthObjectsContainer(
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<NearEarthObject>>
) {
    @JsonClass(generateAdapter = true)
    data class NearEarthObject(
        val id: Long,
        val name: String,
        @Json(name = "nasa_jpl_url") val nasaJplUrl: String,
        @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
        @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameter,
        @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid: Boolean,
        @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
        @Json(name = "orbital_data") val orbitalData: OrbitalData? = null,
    ) {
        @JsonClass(generateAdapter = true)
        data class EstimatedDiameter(
            @Json(name = "meters") val range: EstimatedDiameterRange
        ) {
            @JsonClass(generateAdapter = true)
            data class EstimatedDiameterRange(
                @Json(name = "estimated_diameter_min") val min: Double,
                @Json(name = "estimated_diameter_max") val max: Double,
            )
        }

        @JsonClass(generateAdapter = true)
        data class CloseApproachData(
            @Json(name = "close_approach_date") val date: String,
            @Json(name = "epoch_date_close_approach") val epochDate: Long,
            @Json(name = "relative_velocity") val relativeVelocity: RelativeVelocity,
            @Json(name = "miss_distance") val missDistance: MissDistance,
        ) {
            @JsonClass(generateAdapter = true)
            data class RelativeVelocity(
                @Json(name = "kilometers_per_second") val kilometersPerSecond: Double
            )

            @JsonClass(generateAdapter = true)
            data class MissDistance(
                val kilometers: Double,
                val lunar: Double,
            )
        }

        @JsonClass(generateAdapter = true)
        data class OrbitalData(
            @Json(name = "orbit_class") val orbitClass: OrbitClass
        ) {
            @JsonClass(generateAdapter = true)
            data class OrbitClass(
                @Json(name = "orbit_class_description") val description: String
            )
        }
    }
}

fun NearEarthObjectsContainer.asDbModel(): Array<DbAsteroid> {
    return nearEarthObjects.entries.flatMap { it.value }.map {
        DbAsteroid(
            id = it.id,
            codename = it.name,
            closeApproachDate = it.closeApproachData[0].date,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter.range.max,
            relativeVelocity = it.closeApproachData[0].relativeVelocity.kilometersPerSecond,
            distanceFromEarth = it.closeApproachData[0].missDistance.kilometers,
            isPotentiallyHazardous = it.isPotentiallyHazardousAsteroid
        )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class AstronomyPictureOfTheDay(
    val copyright: String?,
    val date: String,
    val explanation: String,
    @Json(name = "media_type") val mediaTypeString: String,
    @Json(name = "service_version") val serviceVersion: String,
    val title: String,
    val url: String
)