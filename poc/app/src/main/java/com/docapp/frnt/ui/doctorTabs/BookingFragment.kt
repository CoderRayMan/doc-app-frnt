package com.docapp.frnt.ui.doctorTabs

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.docapp.frnt.PaymentActivity
import com.docapp.frnt.SecondFragment
import com.docapp.frnt.data.companions.CommonFunctions.apiCallBookAppointmentServ
import com.docapp.frnt.data.companions.CommonFunctions.apiCallGetDocuDetails
import com.docapp.frnt.data.companions.CommonFunctions.apiCallGetPresDetails
import com.docapp.frnt.data.companions.CommonFunctions.getDocDetails
import com.docapp.frnt.data.companions.CommonFunctions.getSerializableExtra
import com.docapp.frnt.data.companions.Constants.DATE_DAY_DATE_DD_MM_YYYY
import com.docapp.frnt.data.companions.Constants.DATE_DD_MM_YYYY
import com.docapp.frnt.data.companions.Constants.UUID
import com.docapp.frnt.data.model.AppointmentInfo
import com.docapp.frnt.data.model.DoctorDetails
import com.docapp.frnt.databinding.FragmentBookingBinding
import com.docapp.frnt.ui.doctorCards.ButtonClickCallBack
import com.docapp.frnt.ui.doctorCards.DoctorTimeGridAdapter
import org.json.JSONObject
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.Calendar.getInstance
import java.util.Date
import java.util.Locale

class BookingFragment(
    private var docDetails: DoctorDetails,
    private var tabsCallback: SecondFragment
) : Fragment(), ButtonClickCallBack {
    private var _binding: FragmentBookingBinding? = null
    private var dateSelected: Calendar = getInstance()
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var launcherDocumentConfirmation: ActivityResultLauncher<Intent>
    private lateinit var paymentLauncher: ActivityResultLauncher<Intent>
    private lateinit var timeSelected: String
    private var file: DocumentFile? = null
    private lateinit var formattedDate: String
    private lateinit var appoInfo: AppointmentInfo
    private var uuid: BigInteger? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        launcherDocumentConfirmation =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onFileSelected(result)
            }
        paymentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                payCallBack(result)
            }
        refreshTime(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDate.setOnClickListener {
            setDateTimeField(layoutInflater)
        }
    }

    private fun onFileSelected(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            file = DocumentFile.fromSingleUri(requireContext(), result.data?.data!!)
            initiateBooking()
        }

    }

    private fun setDateTimeField(inflater: LayoutInflater) {
        val newCalendar: Calendar = dateSelected
        datePickerDialog = DatePickerDialog(
            binding.root.context,
            { _, year, monthOfYear, dayOfMonth ->
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0)
                refreshTime(inflater)
            },
            newCalendar.get(YEAR),
            newCalendar.get(MONTH),
            newCalendar.get(DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun refreshTime(inflater: LayoutInflater) {

        binding.textviewSecond.text = SimpleDateFormat(
            DATE_DAY_DATE_DD_MM_YYYY,
            Locale.US
        ).format(Date.from(dateSelected.toInstant()))
        val dayNum = LocalDateTime.ofInstant(
            dateSelected.toInstant(),
            ZoneId.systemDefault()
        ).dayOfWeek.value
        formattedDate = SimpleDateFormat(
            DATE_DD_MM_YYYY,
            Locale.US
        ).format(Date.from(dateSelected.toInstant()))
        val timeList = docDetails.timeList?.get(dayNum - 1)!!

        binding.timeGrid.adapter =
            DoctorTimeGridAdapter(
                inflater,
                timeList,
                launcherDocumentConfirmation,
                docDetails.bookedAppointments?.get(formattedDate),
                this,
                LocalDateTime.ofInstant(
                    dateSelected.toInstant(),
                    ZoneId.systemDefault()
                ).dayOfYear == LocalDateTime.ofInstant(
                    Instant.now(), ZoneId.systemDefault()
                ).dayOfYear
            )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setTime(timeSelected: String) {
        this.timeSelected = timeSelected
    }

    override fun initiateBooking() {
        val dateString = "$formattedDate $timeSelected"
        uuid = getSerializableExtra(requireActivity().intent, UUID, BigInteger::class.java)
        appoInfo = AppointmentInfo(dateString, docDetails.dctId, uuid!!)
        val confirmationString =
            "You are about to pay INR.${docDetails.feesInr} to Dr.${docDetails.dctName} for a booking on $formattedDate at $timeSelected."
        payTheFees(confirmationString)
    }

    private fun payTheFees(confirmation: String) {
        AlertDialog.Builder(binding.root.context)
            .setTitle("Payments")
            .setMessage("$confirmation Do you want to proceed?")
            .setPositiveButton("Confirm and Pay") { _: DialogInterface?, _: Int ->
                val paymentparams = JSONObject()
                paymentparams.put("name", "$uuid")
                paymentparams.put(
                    "description",
                    "payment - INR.${docDetails.feesInr} to Dr-Id: ${docDetails.dctId} from $uuid"
                )
                paymentparams.put("amount", docDetails.feesInr * 100)
                paymentparams.put("currency", "INR")
                val paymentActivity = Intent(binding.root.context, PaymentActivity::class.java)
                paymentActivity.putExtra("payload", paymentparams.toString())
                paymentLauncher.launch(paymentActivity)
            }
            .setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                Toast.makeText(binding.root.context, "Process Cancelled", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun payCallBack(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK)
            saveAppointment()
        else {
            Toast.makeText(
                binding.root.context,
                "Booking Failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveAppointment() {
        val ff = apiCallBookAppointmentServ(binding.root.context, file, appoInfo)
        Toast.makeText(layoutInflater.context, ff, Toast.LENGTH_SHORT).show()
        docDetails = getDocDetails(layoutInflater.context, docDetails.dctId)!!
        refreshTime(layoutInflater)
        tabsCallback.refreshPatientDetails(
            apiCallGetDocuDetails(
                binding.root.context,
                docDetails.dctId,
                uuid!!
            ), apiCallGetPresDetails(binding.root.context, docDetails.dctId, uuid!!)
        )
    }


}