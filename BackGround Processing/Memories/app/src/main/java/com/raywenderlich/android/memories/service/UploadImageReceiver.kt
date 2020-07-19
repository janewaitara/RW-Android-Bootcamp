package com.raywenderlich.android.memories.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val ACTION_IMAGE_UPLOAD = "images_upload"
class UploadImageReceiver (
    private inline val onImagesUpload: (Boolean) -> Unit) :
    BroadcastReceiver(){
        //on receiving the intent of the action declared , it involves a callback function
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_IMAGE_UPLOAD){
                val isUploaded = intent.getBooleanExtra("is_uploaded", false)

                onImagesUpload(isUploaded)
            }
        }

    }