package com.udacity.asteroidradar.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM dbasteroid")
    suspend fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DbAsteroid)
}

@Database(entities = [DbAsteroid::class], version = 1, exportSchema = true)
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
                    "videos"
                ).build()
        }
    }
    return INSTANCE
}