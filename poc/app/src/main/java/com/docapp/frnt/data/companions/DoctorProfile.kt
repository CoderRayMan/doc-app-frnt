package com.docapp.frnt.data.companions

import com.docapp.frnt.data.model.LoggedInUser
import com.squareup.moshi.JsonClass
import java.math.BigInteger

@JsonClass(generateAdapter = true)
data class DoctorProfile(
    val dctId: BigInteger,
    val dctName: String,
    val location: String,
    val specialisation: String,
    val yrsOfExp: Int?,
    val timeList: List<List<String>>?,
    val bookedAppointments: Map<String, Set<String>>?,
    val feesInr: Float,
    val pendingPayment: Float,
    val profile: LoggedInUser
)
