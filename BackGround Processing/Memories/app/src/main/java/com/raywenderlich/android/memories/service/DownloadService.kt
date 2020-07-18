package com.raywenderlich.android.memories.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast
import java.io.File

class DownloadService: Service (){
    //used when you want a bounce service
    override fun onBind(intent: Intent?): IBinder? {
       return null
    }

    //used when you start a service the regular way and receives an intent and necessary params

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //we'll use it to start a download image work

        val imagePath = intent?.getStringExtra("image_path")
        if (imagePath != null){
            downloadImage(imagePath)
        }else{
            Log.d("Missing image path", "Stopping service")
            stopSelf() //stops the service from within and frees up the resources
        }
        /**if the originating process is killed while the service is being started ,
         * the service will be removed from the started state
         * You don't have to explicitly start the service yourself and don't waste any resources*/
        return START_NOT_STICKY
    }

    private fun downloadImage(imagePath: String){
        Thread(Runnable {
            val file = File(applicationContext.externalMediaDirs.first(), imagePath)

            FileUtils.downloadImage(file, imagePath)
        }).start()
    }

    override fun onDestroy() {
        //shows when the service is stopped
        applicationContext?.toast("Stopping service")
        super.onDestroy()
    }
}