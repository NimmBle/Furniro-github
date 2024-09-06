package com.example.service.impl

import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.example.service.AzureBlobService
import io.ktor.http.content.*
import java.io.InputStream

class AzureBlobServiceImpl(connectionString: String, containerName: String) : AzureBlobService {

    private val blobContainerClient: BlobContainerClient

    init {
        val blobServiceClient = BlobServiceClientBuilder()
            .connectionString(connectionString)
            .buildClient()

        blobContainerClient = blobServiceClient.getBlobContainerClient(containerName)
    }

    override suspend fun uploadImage(data: PartData.FileItem): String {

        val imageFileName = data.originalFileName
        val imageStream = data.streamProvider()

        if (imageFileName == null) {
            throw IllegalArgumentException("Image name is required")
        }

        val blobClient = blobContainerClient.getBlobClient(imageFileName)

        blobClient.upload(imageStream, true)

        return blobClient.blobUrl
    }

    override fun deleteImage(imageName: String) {
        val blobClient = blobContainerClient.getBlobClient(imageName)

        if (blobClient.exists()) {
            blobClient.delete()
        } else {
            throw IllegalArgumentException("Image $imageName does not exist")
        }
    }
}