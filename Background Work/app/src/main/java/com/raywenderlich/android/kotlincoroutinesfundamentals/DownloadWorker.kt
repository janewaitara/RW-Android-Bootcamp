package com.raywenderlich.android.kotlincoroutinesfundamentals

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadWorker(context: Context, workerParameters: WorkerParameters)
    :Worker(context, workerParameters){
    //describes what work to run and returns a result which can be a success or failure
    override fun doWork(): Result {

        val imageUrl = URL("https://wallpaperplay.com/walls/full/1/c/7/38027.jpg")
        val connection = imageUrl.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        val imagePath = "owl_image.jpg"
        val inputStream = connection.inputStream
        val file = File(applicationContext.externalMediaDirs.first(), imagePath) //a file to store the image in external directory

        val outputStream = FileOutputStream(file)
        outputStream.use { output->
            val buffer = ByteArray(4 * 1024) //reads the data slowly in a buffered way

            var byteCount = inputStream.read(buffer)

            while (byteCount > 0){
                output.write(buffer, 0, byteCount)

                byteCount = inputStream.read(buffer)
            }

            output.flush()
        }

        return Result.success()
    }
}