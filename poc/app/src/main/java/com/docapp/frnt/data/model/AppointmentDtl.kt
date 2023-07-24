package com.docapp.frnt.data.model

import com.squareup.moshi.JsonClass
import java.math.BigInteger
import java.time.Instant

@JsonClass(generateAdapter = true)
data class AppointmentDtl(
    val dctId: BigInteger,
    val patientId: BigInteger,
    val appoId: BigInteger,
    val appointmentDate: Instant,
    val appointmentTime: Instant,
    val status: String,
    val doctorName: String,
    val patientName: String
)
