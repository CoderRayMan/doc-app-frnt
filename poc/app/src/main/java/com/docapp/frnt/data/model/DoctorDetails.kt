package com.docapp.frnt.data.model

import com.squareup.moshi.JsonClass
import java.math.BigInteger

@JsonClass(generateAdapter = true)
data class DoctorDetails(
    val dctId: BigInteger,
    val dctName: String,
    val location: String,
    val specialisation: String,
    val yrsOfExp: Int?,
    val timeList: List<List<String>>?,
    val bookedAppointments: Map<String, Set<String>>?,
    val feesInr: Float

)
