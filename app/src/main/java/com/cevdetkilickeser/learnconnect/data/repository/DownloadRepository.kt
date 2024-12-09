package com.cevdetkilickeser.learnconnect.data.repository

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import com.cevdetkilickeser.learnconnect.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadRepository @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun downloadVideo(videoUrl: String, title: String): String? = withContext(Dispatchers.IO) {
        val request = DownloadManager.Request(Uri.parse(videoUrl))
        request.setTitle(context.getString(R.string.downloading))
        request.setDescription(context.getString(R.string.downloading))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        val destinationUri = getDownloadDestinationUri(title)
        request.setDestinationUri(destinationUri)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        return@withContext destinationUri.toString()
    }

    private suspend fun getDownloadDestinationUri(title: String): Uri = withContext(Dispatchers.IO) {
        val fileName = "${title.replace(" ", "_")}.mp4"
        val destinationDirectory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.getExternalFilesDir(null)?.absolutePath
        } else {
            "/storage/emulated/0/Download"
        }
        return@withContext Uri.parse("file://$destinationDirectory/$fileName")
    }
}