package com.docapp.frnt.data.model

import com.squareup.moshi.JsonClass
import java.math.BigInteger

@JsonClass(generateAdapter = true)
data class AppointmentInvoice(
    val appoId: BigInteger
)
