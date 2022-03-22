package com.ahmed.composepayground

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.ahmed.composepayground.datastore.AppSettings
import com.ahmed.composepayground.datastore.AppSettingsSerializer
import com.ahmed.composepayground.datastore.Language
import com.ahmed.composepayground.ui.theme.ComposePaygroundTheme
import kotlinx.coroutines.launch

val Context.dataStore by dataStore(
    fileName = "app-settings.json",
    serializer = AppSettingsSerializer
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
      * Motion layout
      * */
        setContent {
            //datastore
            ComposePaygroundTheme {
                val appSettings by dataStore.data.collectAsState(initial = AppSettings())
                val scope = rememberCoroutineScope()
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (i in 0..2){
                        val currentLang = Language.values()[i]
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = currentLang == appSettings.language, onClick = {
                                scope.launch {
                                    setLanguage(language =  currentLang)
                                }
                            })
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = currentLang.toString())
                        }
                    }

                }

            }
            //motionlayout
            /*
            ComposePaygroundTheme {
                Column {
                    var progress by remember {
                        mutableStateOf(0f)
                    }
                    ProfileHeader(progress = progress)
                    Spacer(modifier = Modifier.height(32.dp))
                    Slider(value = progress, onValueChange = {progress = it}, modifier = Modifier.padding(32.dp))

                }
            }*/
        }

        /*
        * work manager example
        * */
        /* val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>().setConstraints(
             Constraints.Builder()
                 .build()
         ).build()

         val colorFilterWorker = OneTimeWorkRequestBuilder<ColorFilterWorker>().setConstraints(
             Constraints.Builder()
                 .setRequiredNetworkType(NetworkType.CONNECTED)
                 .build()
         ).build()

         val workManager = WorkManager.getInstance(applicationContext)
         setContent {
             ComposePaygroundTheme {
                 val workInfos =
                     workManager.getWorkInfosForUniqueWorkLiveData("download").observeAsState().value
                 val downloadInfo = remember(key1 = workInfos) {
                     workInfos?.find { it.id == downloadRequest.id }
                 }

                 val filterInfo = remember(key1 = workInfos) {
                     workInfos?.find { it.id == colorFilterWorker.id }
                 }

                 val imageUri by derivedStateOf {
                     val downloadUri =
                         downloadInfo?.outputData?.getString(WorkerKeys.IMAGE_URI)?.toUri()
                     val filterUri =
                         filterInfo?.outputData?.getString(WorkerKeys.FILTER_URI)?.toUri()
                     filterUri ?: downloadUri
                 }
                 Column(
                     Modifier.fillMaxSize(),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     imageUri?.let {
                         Image(
                             painter = rememberImagePainter(data = it),
                             contentDescription = null,
                             modifier = Modifier.fillMaxWidth()
                         )
                         Spacer(modifier = Modifier.height(16.dp))

                     }

                     Button(onClick = {
                         workManager.beginUniqueWork(
                             "download",
                             ExistingWorkPolicy.KEEP,
                             downloadRequest
                         ).then(colorFilterWorker).enqueue()
                     }, enabled = downloadInfo?.state != WorkInfo.State.RUNNING) {
                         Text(text = "Start download")
                     }
                     Spacer(modifier = Modifier.height(16.dp))
                     when(downloadInfo?.state){
                         WorkInfo.State.ENQUEUED -> Text(text = "Download enqueued")
                         WorkInfo.State.RUNNING -> Text(text = "Downloading...")
                         WorkInfo.State.SUCCEEDED -> Text(text = "Download Succeeded")
                         WorkInfo.State.FAILED -> Text(text = "Download failed")
                         WorkInfo.State.BLOCKED -> Text(text = "Blocked")
                         WorkInfo.State.CANCELLED -> Text(text = "Download Canceled")
                         null -> {}
                     }

                     when(filterInfo?.state){
                         WorkInfo.State.ENQUEUED -> Text(text = "Filter enqueued")
                         WorkInfo.State.RUNNING -> Text(text = "Filtering...")
                         WorkInfo.State.SUCCEEDED -> Text(text = "Filter Succeeded")
                         WorkInfo.State.FAILED -> Text(text = "Filter failed")
                         WorkInfo.State.BLOCKED -> Text(text = "Filter Blocked")
                         WorkInfo.State.CANCELLED -> Text(text = "Filter Canceled")
                         null -> {
                         }
                     }


                 }
             }
         }*/
    }

    private suspend fun setLanguage(language: Language) {
        dataStore.updateData {
            it.copy(language = language)
        }
    }
}



