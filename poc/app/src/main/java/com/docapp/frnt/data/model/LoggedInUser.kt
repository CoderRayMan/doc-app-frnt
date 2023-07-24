package com.docapp.frnt.data.model

import com.squareup.moshi.JsonClass
import java.math.BigInteger

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@JsonClass(generateAdapter = true)
data class LoggedInUser(
        val uuid: BigInteger,
        val name: String,
        val userEmail: String,
        val profilePicLocation: String?

)