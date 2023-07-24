package com.docapp.frnt.data.companions

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

object InstantAdapter {
    @FromJson
    fun fromJson(string: String) = Instant.parse(string)

    @ToJson
    fun toJson(value: Instant) = value.toString()
}