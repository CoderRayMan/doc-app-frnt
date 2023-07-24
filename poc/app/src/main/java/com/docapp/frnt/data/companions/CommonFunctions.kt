package com.docapp.frnt.data.companions

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.documentfile.provider.DocumentFile
import com.docapp.frnt.R
import com.docapp.frnt.data.Result
import com.docapp.frnt.data.companions.APIDataSource.getTransaction
import com.docapp.frnt.data.companions.APIDataSource.postTransactionForm
import com.docapp.frnt.data.model.AppointmentDtl
import com.docapp.frnt.data.model.AppointmentInfo
import com.docapp.frnt.data.model.AppointmentInvoice
import com.docapp.frnt.data.model.DoctorDetails
import com.docapp.frnt.data.model.FileInfo
import com.docapp.frnt.data.model.PagedDoctorDetails
import java.math.BigInteger
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object CommonFunctions {
    fun <T : java.io.Serializable> getSerializableExtra(
        intent: Intent, name: String,
        clazz: Class<T>
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(name, clazz)
        } else {
            intent.extras!!.get(name) as T
        }
    }

    fun <T : java.io.Serializable> getSerializable(
        bundle: Bundle, name: String,
        clazz: Class<T>
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getSerializable(name, clazz)
        } else {
            bundle.get(name) as T
        }
    }

    fun getDocDetails(
        context: Context,
        docId: BigInteger
    ): DoctorDetails? {
        val doctorDetails =
            getTransaction<DoctorDetails>(
                "${context.getString(R.string.backend_base)}/${context.getString(R.string.doctor_details_endpoint)}/$docId"
            )
        if (doctorDetails is Result.Success<DoctorDetails>) return doctorDetails.data
        return null
    }

    fun getDocProfile(
        context: Context,
        docId: BigInteger
    ): DoctorProfile? {
        val doctorDetails =
            getTransaction<DoctorProfile>(
                "${context.getString(R.string.backend_base)}/${context.getString(R.string.doctor_details_endpoint)}/$docId?get-full-profile=true"
            )
        if (doctorDetails is Result.Success<DoctorProfile>) return doctorDetails.data
        return null
    }

    fun getDocDetailsAll(context: Context): PagedDoctorDetails? {
        val doctorDetails =
            getTransaction<PagedDoctorDetails>(
                "${context.getString(R.string.backend_base)}/${context.getString(R.string.doctor_details_endpoint)}"
            )
        if (doctorDetails is Result.Success<PagedDoctorDetails>) return doctorDetails.data
        return null
    }

    fun apiCallBookAppointmentServ(
        context: Context, file: DocumentFile?,
        appoInfo: AppointmentInfo
    ): String {
        val url = "${context.getString(R.string.backend_base)}/${
            context.getString(R.string.booking_endpoint)
        }/${context.getString(R.string.book_appointment_endpoint)}"
        val appoData = postTransactionForm<AppointmentInvoice>(
            context.contentResolver,
            appoInfo,
            file,
            url
        )
        return if (appoData is Result.Success<AppointmentInvoice>) " Success!! Your Booking Id ${appoData.data.appoId}" else "Failed"

    }

    fun apiCallGetDocuDetails(
        context: Context,
        dctId: BigInteger? = null,
        patientId: BigInteger
    ): List<FileInfo> {
        val url = dctId?.let {
            "${context.getString(R.string.backend_base)}/${context.getString(R.string.booking_endpoint)}/${
                context.getString(R.string.bookings_document_endpoint)
            }/${
                context.getString(
                    R.string.bookings_document_with_doc_filter_endpoint,
                    patientId,
                    dctId
                )
            }"
        } ?: let {
            "${context.getString(R.string.backend_base)}/${
                context.getString(R.string.booking_endpoint)
            }/${context.getString(R.string.bookings_document_endpoint)}/$patientId"
        }
        val result = getTransaction<List<FileInfo>>(url)
        return if (result is Result.Success<List<FileInfo>>) result.data else listOf()
    }

    fun apiCallGetPresDetails(
        context: Context,
        dctId: BigInteger? = null,
        patientId: BigInteger
    ): List<AppointmentDtl> {
        val url = dctId?.let {
            "${context.getString(R.string.backend_base)}/${context.getString(R.string.booking_endpoint)}/${
                context.getString(
                    R.string.bookings_document_with_doc_filter_endpoint,
                    patientId,
                    dctId
                )
            }"
        } ?: let {
            "${context.getString(R.string.backend_base)}/${
                context.getString(R.string.booking_endpoint)
            }/$patientId"
        }
        val result = getTransaction<List<AppointmentDtl>>(url)
        return if (result is Result.Success<List<AppointmentDtl>>) result.data else listOf()
    }

    fun apiCallGetDocAppointments(
        context: Context,
        dctId: BigInteger,
        patientId: BigInteger? = null
    ): List<AppointmentDtl> {
        val url = patientId?.let {
            "${context.getString(R.string.backend_base)}/${context.getString(R.string.booking_endpoint)}/doctors/${dctId}?for-patient=${patientId}"
        } ?: let {
            "${context.getString(R.string.backend_base)}/${context.getString(R.string.booking_endpoint)}/doctors/${dctId}"
        }
        val result = getTransaction<List<AppointmentDtl>>(url)
        return if (result is Result.Success<List<AppointmentDtl>>) result.data else listOf()
    }

    fun parseTimeString(timeString: String): LocalTime? {
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        return try {
            LocalTime.parse(timeString, formatter)
        } catch (e: Exception) {
            null
        }
    }

}