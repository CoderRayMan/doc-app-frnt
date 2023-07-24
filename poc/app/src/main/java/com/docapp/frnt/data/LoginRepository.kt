package com.docapp.frnt.data

import com.docapp.frnt.data.companions.APIDataSource.postTransaction
import com.docapp.frnt.data.model.LoggedInUser
import com.docapp.frnt.data.model.LoginReqModel

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    // in-memory cache of the loggedInUser object
    private var user: LoggedInUser? = null

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
//TODO logout functionality
    }

    fun login(username: String, password: String, uri: String): Result<LoggedInUser> {
        // handle login
        val result = postTransaction<LoggedInUser>(
            LoginReqModel(username, password),
            uri
        )

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}