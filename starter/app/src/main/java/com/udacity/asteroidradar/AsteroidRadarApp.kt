package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

lateinit var NASA_API_KEY: String
lateinit var TODAY_DATE_FORMATTED: String

class AsteroidRadarApp : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .apply {
                setRequiredNetworkType(NetworkType.UNMETERED)
                setRequiresBatteryNotLow(true)
                setRequiresCharging(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance()
            .enqueueUniquePeriodicWork(
                RefreshDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
    }

    override fun onCreate() {
        super.onCreate()
        NASA_API_KEY = getString(R.string.nasa_api_key)
        TODAY_DATE_FORMATTED = todayDateFormatted
        delayedInit()
    }

    private val todayDateFormatted =
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            .format(Calendar.getInstance().time)
}