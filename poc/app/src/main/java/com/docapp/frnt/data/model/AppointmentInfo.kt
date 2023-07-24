package com.docapp.frnt.data.model

import java.math.BigInteger

data class AppointmentInfo(
    val appointmentDateTime: String,
    val docId: BigInteger,
    val patientId: BigInteger,
)
