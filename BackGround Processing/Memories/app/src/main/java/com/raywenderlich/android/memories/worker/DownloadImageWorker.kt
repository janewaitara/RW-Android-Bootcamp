package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.raywenderlich.android.memories.networking.BASE_URL
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

        val imageUrl = URL("$BASE_URL/files/$imageDownloadPath") //download the image from the server directly

        val connection = imageUrl.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        val imagePath = parts.last() //the last part of the image as its name
        val inputStream = connection.inputStream
        val file = File(applicationContext.externalMediaDirs.first(), imagePath)

        //output code to store the bytes into a file
        val outputStream = FileOutputStream(file)
        outputStream.use { output->
            val buffer = ByteArray(4 * 1024)
            var byteCount = inputStream.read(buffer)

            while (byteCount > 0){
                output.write(buffer,0,byteCount)
                byteCount = inputStream.read(buffer)
            }

            output.flush()
        }
        val output = workDataOf("image_path" to file.absolutePath)
        return Result.success(output)
    }
}