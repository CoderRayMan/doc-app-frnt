package com.docapp.frnt.ui.login

import java.math.BigInteger

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val profilePicLocation: String?,
    //... other data fields that may be accessible to the UI
    val uuid: BigInteger,
    val userEmail: String
)