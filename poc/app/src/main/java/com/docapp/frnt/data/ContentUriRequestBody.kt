package com.docapp.frnt.data

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.IOException

class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val contentUri: Uri,
    private val type: String?
) : RequestBody() {

    override fun contentType(): MediaType? {
        return type?.toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        val inputStream = contentResolver.openInputStream(contentUri)
            ?: throw IOException("Couldn't open content URI for reading")

        inputStream.source().use { source ->
            sink.writeAll(source)
        }
        inputStream.close()
    }
}