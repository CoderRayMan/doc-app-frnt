package com.docapp.frnt.data.companions

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigInteger

object BigIntegerAdapter {
    @FromJson
    fun fromJson(string: String) = BigInteger(string)

    @ToJson
    fun toJson(value: BigInteger) = value.toString()
}