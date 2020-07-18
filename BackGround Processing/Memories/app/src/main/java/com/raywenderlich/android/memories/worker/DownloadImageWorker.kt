package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.raywenderlich.android.memories.networking.BASE_URL
import com.raywenderlich.android.memories.utils.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageWorker(context : Context, workerParameters: WorkerParameters ):
Worker(context, workerParameters){
    override fun doWork() :Result{

        val isAlreadyDownload = inputData.getBoolean("is_downloaded", false)
        //creating a file to save the image
        val imageDownloadPath = inputData.getString("image_path") ?: return Result.failure()
        val parts = imageDownloadPath.split("/")

        //check if the file exists to make sure you don't download the image more than once
        if (isAlreadyDownload){
            val imageFile = File(applicationContext.externalMediaDirs.first(), parts.last())
            return Result.success(workDataOf("image_path" to imageFile.absolutePath))
        }

        val imagePath = parts.last() //the last part of the image as its name
        val file = File(applicationContext.externalMediaDirs.first(), imagePath)
        FileUtils.downloadImage(file,imagePath)

        val output = workDataOf("image_path" to file.absolutePath)
        return Result.success(output)
    }
}