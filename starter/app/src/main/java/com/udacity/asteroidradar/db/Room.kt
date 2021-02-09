package com.udacity.asteroidradar.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM dbasteroid WHERE closeApproachDate >= :sinceDate ORDER BY closeApproachDate, isPotentiallyHazardous DESC, distanceFromEarth")
    fun getAsteroids(sinceDate: String): LiveData<List<DbAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DbAsteroid)
}

@Database(entities = [DbAsteroid::class], version = 1, exportSchema = false)
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