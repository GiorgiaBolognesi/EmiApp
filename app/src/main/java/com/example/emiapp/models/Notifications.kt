package com.example.emiapp.models

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.emiapp.R
import com.example.emiapp.activities.MainActivity
import java.util.concurrent.TimeUnit

fun runInstantWorker(context: Context) {
    val durationWorker =
        OneTimeWorkRequestBuilder<Notifications>()
            .build()

    WorkManager
        .getInstance(context)
        .enqueue(durationWorker)
}

fun startPeriodicWorker(context: Context) {
    val durationWorker =
        PeriodicWorkRequestBuilder<Notifications>(12, TimeUnit.HOURS, 30, TimeUnit.MINUTES)
            .build()

    WorkManager
        .getInstance(context)
        .enqueueUniquePeriodicWork("myPeriodicWork", ExistingPeriodicWorkPolicy.REPLACE, durationWorker)
}

class Notifications(val context: Context, params: WorkerParameters): Worker(context,params) {
    var durDays:String = ""
    var intent: Intent = Intent(context, MainActivity::class.java)

    override fun doWork(): Result {
        val appContext = applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPrefsMain", Context.MODE_PRIVATE)
        durDays = sharedPreferences.getString("durDays","").toString()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (durDays == "3") {
            val notificationText = "Non hai un attacco da 3 giorni!"
            val builder = NotificationCompat.Builder(this.context, "10")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Complimenti!")
                .setContentText(notificationText)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            //create notchannel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "name"
                val descriptionText = "non hai un attacco"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("10", name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        notify("100".toInt(), builder.build())
                    }
                }
                else  {
                    notify("100".toInt(), builder.build())
                }
            }
        }
        return Result.success()
    }
}