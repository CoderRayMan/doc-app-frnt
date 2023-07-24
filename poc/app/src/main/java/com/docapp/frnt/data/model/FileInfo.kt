package com.docapp.frnt.data.model

import com.squareup.moshi.JsonClass
import java.math.BigInteger
import java.time.Instant

@JsonClass(generateAdapter = true)
data class FileInfo(
    val fileId: String,
    val filename: String,
    val fileType: String,
    val fileSize: String,
    val appointmentId: BigInteger,
    val patientId: BigInteger,
    val relatedDocId: BigInteger,
    val isPrescription: Boolean,
    val timestamp: Instant
)
