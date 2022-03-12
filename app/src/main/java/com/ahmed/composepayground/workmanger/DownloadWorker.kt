package com.ahmed.composepayground.workmanger

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.*
import com.ahmed.composepayground.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class DownloadWorker(val context:Context,val workerParameters: WorkerParameters) : CoroutineWorker(context,workerParameters) {
    override suspend fun doWork(): Result {
        startForegroundServices()
        delay(5000L)
        val response = FileApi.instance.downloadImage()
        response.body()?.let {body->
            return withContext(Dispatchers.IO){
                val file = File(context.cacheDir,"image.jpg")
                val outputStraem = FileOutputStream(file)
                outputStraem.use {
                    try {
                        it.write(body.bytes())
                    }catch (e:IOException){
                        return@withContext Result.failure(
                            workDataOf(
                                WorkerKeys.ERROR_MSG to e.localizedMessage
                            )
                        )
                    }
                }
                Result.success(
                    workDataOf(
                        WorkerKeys.IMAGE_URI to file.toUri().toString()

                    )
                )
            }
        }

        if (!response.isSuccessful){
            if (response.code().toString().startsWith("5")){
                return Result.retry()
            }
            return Result.failure(
                workDataOf(
                    WorkerKeys.ERROR_MSG to "Invalid server response"
                )
            )
        }

        return Result.failure(
            workDataOf(
                WorkerKeys.ERROR_MSG to "unknown error"
            )
        )
    }

    private suspend fun startForegroundServices(){
        setForegroundAsync(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context,"download_channel")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("Downloading....")
                    .setContentTitle("Downloading in progress..")
                    .build()
            )
        )
    }
}
