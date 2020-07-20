package com.raywenderlich.android.memories.service

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast
import java.io.File


const val SERVICE_NAME ="Download image service"

class DownloadService: JobIntentService(){

    companion object{
        private const val JOB_ID = 10

        fun startWork(context: Context, intent: Intent){
            //takes in a context, the service class to start, job id as an int and an intent to start the service with
            enqueueWork(context, DownloadService::class.java, JOB_ID, intent) //enqueueing work in the system and the system will decide which job api to use(jobScheduler or intentService)
        }
    }


    //starting point for all intentServices
    override fun onHandleWork(intent: Intent) {
        //we'll use it to start a download image work

        val imagePath = intent?.getStringExtra("image_path")

        if (imagePath != null){
            downloadImage(imagePath)
        }else{
            Log.d("Missing image path", "Stopping service")
            stopSelf() //stops the service from within and frees up the resources
        }

    }

    private fun downloadImage(imagePath: String){
        val file = File(applicationContext.externalMediaDirs.first(), imagePath)

        FileUtils.downloadImage(file, imagePath)

    }

    override fun onDestroy() {
        //shows when the service is stopped
        applicationContext?.toast("Stopping service")
        super.onDestroy()
    }
}