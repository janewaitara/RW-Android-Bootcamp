package com.raywenderlich.android.kotlincoroutinesfundamentals

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
//attempts to navigate to the external storage for the app and delete the files
class FileClearWorker(context: Context, workerParameters: WorkerParameters):
        Worker(context, workerParameters) {
    override fun doWork(): Result {

        val root = applicationContext.externalMediaDirs.first()

        return try {
            root.listFiles()?.forEach { child->
                if (child.isDirectory){
                    child.deleteRecursively()
                }else {
                    child.delete()
                }
            }
            Result.success()
        }catch (error: Throwable){
            error.printStackTrace()
            Result.failure()
        }
    }
}