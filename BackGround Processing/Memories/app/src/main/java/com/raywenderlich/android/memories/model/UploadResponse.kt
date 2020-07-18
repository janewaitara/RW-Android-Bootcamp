package com.raywenderlich.android.memories.model

import kotlinx.serialization.Serializable

/**
 * Response from the server for the  message and download url*/
@Serializable
class UploadResponse(val message: String = "", val url: String = "") {
}