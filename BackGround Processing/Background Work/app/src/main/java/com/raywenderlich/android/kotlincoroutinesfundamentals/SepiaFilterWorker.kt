package com.raywenderlich.android.kotlincoroutinesfundamentals

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.raywenderlich.android.introapp.ImageUtils
import java.io.FileOutputStream

class SepiaFilterWorker(context: Context, workerParameters: WorkerParameters):
        Worker(context, workerParameters){
    override fun doWork(): Result {
        //takes in the image path for the downloaded image file then apply sepia filter and save the file again
        val imagePath = inputData.getString("image_path")?: return Result.failure()
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val newBitmap = ImageUtils.applySepiaFilter(bitmap)

        //saving the new bitmap stream to a file
        val outputStream = FileOutputStream(imagePath)
        outputStream.use { output->
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.flush()
        }
       // return the same imagePath  to display the image
        val output = workDataOf("image_path" to imagePath) //workDataOf build the output object of the worker
        return Result.success(output)
    }
}