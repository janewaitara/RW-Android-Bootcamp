package com.raywenderlich.android.memories.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val ACTION_IMAGES_SYNCHRONIZED = "images_synchronize"
class SynchronizeImagesReceiver(
        private inline val onImagesSynchronize: () -> Unit) :
        BroadcastReceiver(){
    //on receiving the intent of the action declared , t involves a callback function
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_IMAGES_SYNCHRONIZED){
            onImagesSynchronize()
        }
    }

}