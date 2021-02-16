package com.udacity.asteroidradar.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query(
        """SELECT DbAsteroid.* FROM DbAsteroid 
            INNER JOIN AsteroidDateFilter ON DbAsteroid.closeApproachDate = AsteroidDateFilter.date
            ORDER BY closeApproachDate, isPotentiallyHazardous DESC, distanceFromEarth"""
    )
    fun getAsteroids(): LiveData<List<DbAsteroid>>

    @Query("SELECT max(closeApproachDate) FROM DbAsteroid")
    suspend fun getMaxDate(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(vararg asteroids: DbAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDates(vararg asteroidDateFilter: AsteroidDateFilter)

    @Query("SELECT closeApproachDate FROM DbAsteroid GROUP BY closeApproachDate ORDER BY closeApproachDate")
    suspend fun getAllDates(): List<String>

    @Query("DELETE FROM AsteroidDateFilter")
    suspend fun deleteAllDates()
}

@Database(
    entities = [DbAsteroid::class, AsteroidDateFilter::class],
    version = 1,
    exportSchema = false
)
abstract class AsteroidsDb : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDb

fun getDatabase(context: Context): AsteroidsDb {
    synchronized(AsteroidsDb::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidsDb::class.java,
                    "asteroids"
                ).build()
        }
    }
    return INSTANCE
}