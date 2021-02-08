package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidsApiStatus  { LOADING, ERROR, DONE }

private const val BASE_URL =
    "https://api.nasa.gov/neo/rest/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidsApiService {
    @GET("feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String)
    : NeoFeed
}

object AsteroidsApi {
    val retrofitApiService: AsteroidsApiService by lazy {
        retrofit.create(AsteroidsApiService::class.java)
    }
}