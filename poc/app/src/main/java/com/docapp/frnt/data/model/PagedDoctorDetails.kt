package com.docapp.frnt.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PagedDoctorDetails(
    val doctorDetailsList: List<DoctorDetails>,
    val pageSize: Int,
    val totalCount: Long
)
