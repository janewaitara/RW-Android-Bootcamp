package com.raywenderlich.android.memories.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class UploadImagesService: JobIntentService() {

    private val remoteApi by lazy { App.remoteApi }

    companion object{
        private const val JOB_ID = 52

        fun startWork(context: Context, intent: Intent){
            //takes in a context, the service class to start, job id as an int and an intent to start the service with
            enqueueWork(context,UploadImagesService::class.java, JOB_ID, intent) //enqueueing work in the system and the system will decide which job api to use(jobScheduler or intentService)
        }
    }

    override fun onHandleWork(intent: Intent) {
       val filePath = intent.getStringExtra("image_path")
        if (filePath != null){
            uploadImage(filePath)
        }
    }

    //called when there is a valid image path
    private fun uploadImage(filePath: String) {
        GlobalScope.launch {
            val result = remoteApi.uploadImage(File(filePath))

            //send a broadcast to the receiver with a result
            val intent = Intent()
            intent.putExtra("is_uploaded", result.message == "Success")
            intent.action = ACTION_IMAGE_UPLOAD

            sendBroadcast(intent)
        }
    }

}