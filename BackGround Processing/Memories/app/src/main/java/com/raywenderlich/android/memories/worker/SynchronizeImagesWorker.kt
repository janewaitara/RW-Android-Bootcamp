package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.raywenderlich.android.memories.utils.FileUtils
import java.io.File

class SynchronizeImagesWorker (context: Context, workerParameters: WorkerParameters):
        Worker(context,workerParameters) {
    override fun doWork(): Result {
        val images = inputData.getStringArray("images") ?: return Result.failure()

        //receives a list of images and downloads each image accordingly
        FileUtils.queueImagesForDownload(applicationContext, images)

        return Result.success()
    }
}