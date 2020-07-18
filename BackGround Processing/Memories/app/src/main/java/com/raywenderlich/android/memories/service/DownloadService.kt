package com.raywenderlich.android.memories.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast
import java.io.File


const val SERVICE_NAME ="Download image service"

class DownloadService: IntentService(SERVICE_NAME){

    //starting point for all intentServices
    override fun onHandleIntent(intent: Intent?) {
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