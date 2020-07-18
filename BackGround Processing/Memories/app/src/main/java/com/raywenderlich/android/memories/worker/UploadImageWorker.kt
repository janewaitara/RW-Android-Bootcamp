package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.raywenderlich.android.memories.App
import java.io.File

class UploadImageWorker(context: Context, workerParameters: WorkerParameters):
    CoroutineWorker(context, workerParameters){

    private val remoteApi by lazy {
        App.remoteApi
    }
    /**Since the remote api fun is a suspend function,
     * it requires a coroutine to work hence we used the coroutine worker*/
    override suspend fun doWork(): Result {
       //receive an image path and upload the image

        val imagePath = inputData.getString("image_path") ?: return Result.failure()

        //using the remote Api to upload the file and returning the result depending on server response
        val result = remoteApi.uploadImage(File(imagePath))
        return if (result.message == "Success"){
            Result.success()
        }else{
            Result.failure()
        }
    }
}