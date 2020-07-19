package com.raywenderlich.android.memories.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.raywenderlich.android.memories.App
import com.raywenderlich.android.memories.model.result.Success
import com.raywenderlich.android.memories.ui.main.MainActivity
import com.raywenderlich.android.memories.utils.FileUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val NOTIFICATION_CHANNEL_NAME = "Synchronize service channel"
const val NOTIFICATION_CHANNEL_ID = "Synchronize ID"

class SynchronizeImagesService: Service() {


    private val remoteApi by lazy { App.remoteApi }
    override fun onBind(intent: Intent?): IBinder?  = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        clearStorage()
        fetchImages()

        return START_NOT_STICKY
    }

    private fun showNotification() {
        //in case the android api level is oreo or higher
        createNotificationChannel()

        //updates the main activity with a new intent if clicked
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent,0)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Synchronization Service")
                .setContentText("Downloading image")
                .setContentIntent(pendingIntent)
                .build()
        //once you start a notification it will be displayed in the notification area until the foreground is stopped or app is killed
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        //notification channels only exist in android oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT //visibility
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }

    }


    private fun fetchImages() {
        GlobalScope.launch {
            //getting the images from the server
            val result = remoteApi.getImages()
            if (result is Success){
                val imagesArray = result.data.map { it.imagePath }.toTypedArray()
                //queueing work in the downloadManager
                FileUtils.queueImagesForDownload(applicationContext, imagesArray)
                //stop foreground notification
                stopForeground(true)
                //once images are downloaded, the notification will be removed and the service will stop being in the foreground
                //send the broadcast with the action defined in the receiver
                sendBroadcast(Intent().apply {
                    action = ACTION_IMAGES_SYNCHRONIZED
                })
            }
        }
    }

    private fun clearStorage() {
        FileUtils.clearLocalStorage(applicationContext)
    }


}