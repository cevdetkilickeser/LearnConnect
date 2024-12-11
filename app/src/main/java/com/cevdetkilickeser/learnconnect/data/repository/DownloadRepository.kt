package com.cevdetkilickeser.learnconnect.data.repository

import android.net.Uri

interface DownloadRepository {

    suspend fun downloadVideo(videoUrl: String, title: String): String?

    suspend fun getDownloadDestinationUri(title: String): Uri
}