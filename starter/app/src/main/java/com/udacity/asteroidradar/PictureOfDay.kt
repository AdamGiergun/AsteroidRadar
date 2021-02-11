package com.udacity.asteroidradar

data class PictureOfDay(
    val mediaType: MediaType,
    val title: String,
    val url: String
) {
    enum class MediaType(val value: String) {
        IMAGE("image"),
        VIDEO("video"),
        UNKNOWN("")
    }
}