package com.example.service

import io.ktor.http.content.*

interface AzureBlobService {

    suspend fun uploadImage(data: PartData.FileItem): String

    fun deleteImage(imageName: String)
}