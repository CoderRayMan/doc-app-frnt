package com.docapp.frnt.data.companions

import android.content.ContentResolver
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.documentfile.provider.DocumentFile
import com.docapp.frnt.data.ContentUriRequestBody
import com.docapp.frnt.data.Result
import com.docapp.frnt.data.companions.Constants.APPLICATION_JSON
import com.docapp.frnt.data.companions.Constants.APPLICATION_OCTET_STREAM
import com.docapp.frnt.data.companions.Constants.APPOINTMENT_INFO
import com.docapp.frnt.data.companions.Constants.ERROR_IN_API
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
object APIDataSource {
    inline fun <reified T : Any> postTransaction(
        requestBody: Any,
        uri: String, headers: Headers? = null,
    ): Result<T> {
        val client = OkHttpClient()
        val gson = Gson()
        val moshi = Moshi.Builder()
            .add(BigIntegerAdapter)
            .add(InstantAdapter)
            .add(KotlinJsonAdapterFactory()).build()
        val gistJsonAdapter = moshi.adapter<T>(object : TypeToken<T>() {}.type)
        val policy: ThreadPolicy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val requestBodyReformed = gson.toJson(requestBody)
                .toRequestBody(APPLICATION_JSON.toMediaTypeOrNull())
            val request = headers?.let {
                Request.Builder()
                    .url(uri)
                    .headers(headers)
                    .post(requestBodyReformed)
                    .build()
            } ?: let {
                Request.Builder()
                    .url(uri)
                    .post(requestBodyReformed)
                    .build()
            }
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.Error(IOException("$ERROR_IN_API ${response.code}"))
                }
                val responseBody = gistJsonAdapter.fromJson(response.body.source())!!
                return Result.Success(responseBody)

            }
        } catch (e: Throwable) {
            return Result.Error(IOException(ERROR_IN_API, e))
        }

    }


    inline fun <reified T : Any> postTransactionForm(
        contentResolver: ContentResolver,
        requestBody: Any,
        file: DocumentFile?,
        uri: String, headers: Headers? = null,
    ): Result<T> {
        val client = OkHttpClient()
        val gson = Gson()
        val moshi = Moshi.Builder()
            .add(BigIntegerAdapter)
            .add(InstantAdapter)
            .add(KotlinJsonAdapterFactory()).build()
        val gistJsonAdapter = moshi.adapter<T>(object : TypeToken<T>() {}.type)
        val policy: ThreadPolicy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val requestBodyReformed = gson.toJson(requestBody)
                .toRequestBody(APPLICATION_JSON.toMediaTypeOrNull())

            val multipartBody = file?.let {

                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        file.name!!,
                        ContentUriRequestBody(contentResolver, file.uri, APPLICATION_OCTET_STREAM)
                    )
                    .addFormDataPart(APPOINTMENT_INFO, null, requestBodyReformed)
                    .build()
            } ?: let {
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(APPOINTMENT_INFO, null, requestBodyReformed)
                    .build()
            }

            val request = headers?.let {
                Request.Builder()
                    .url(uri)
                    .headers(headers)
                    .post(multipartBody)
                    .build()
            } ?: let {
                Request.Builder()
                    .url(uri)
                    .post(multipartBody)
                    .build()
            }
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.Error(IOException("$ERROR_IN_API ${response.code}"))
                }
                val responseBody = gistJsonAdapter.fromJson(response.body.source())!!
                return Result.Success(responseBody)

            }
        } catch (e: Throwable) {
            return Result.Error(IOException(ERROR_IN_API, e))
        }

    }

    inline fun <reified T : Any> getTransaction(
        uri: String, headers: Headers? = null
    ): Result<T> {
        val client = OkHttpClient()
        val moshi = Moshi.Builder()
            .add(BigIntegerAdapter)
            .add(InstantAdapter)
            .add(KotlinJsonAdapterFactory()).build()
        val gistJsonAdapter = moshi.adapter<T>(object : TypeToken<T>() {}.type)
        val policy: ThreadPolicy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val request = headers?.let {
                Request.Builder()
                    .url(uri)
                    .headers(it)
                    .get()
                    .build()
            } ?: let {
                Request.Builder()
                    .url(uri)
                    .get()
                    .build()
            }
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.Error(IOException("$ERROR_IN_API ${response.code}"))
                }
                val responseBody = gistJsonAdapter.fromJson(response.body.source())!!
                return Result.Success(responseBody)

            }
        } catch (e: Throwable) {
            return Result.Error(IOException(ERROR_IN_API, e))
        }

    }
}