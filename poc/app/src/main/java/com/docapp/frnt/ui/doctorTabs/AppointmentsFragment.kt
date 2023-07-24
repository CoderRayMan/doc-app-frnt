package com.docapp.frnt.ui.doctorTabs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.docapp.frnt.data.companions.CommonFunctions
import com.docapp.frnt.data.companions.Constants
import com.docapp.frnt.data.model.AppointmentDtl
import com.docapp.frnt.data.model.SharedDocuList
import com.docapp.frnt.databinding.AppointmentsLayoutBinding
import com.docapp.frnt.ui.appo.AppointmentAdapter
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AppointmentsFragment : Fragment() {
    private lateinit var appointmentsLayoutBinding: AppointmentsLayoutBinding
    private lateinit var sharedDocuList: SharedDocuList
    private lateinit var uuid: BigInteger
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appointmentsLayoutBinding = AppointmentsLayoutBinding.inflate(inflater, container, false)
        uuid = CommonFunctions.getSerializableExtra(
            requireActivity().intent,
            Constants.UUID,
            BigInteger::class.java
        )!!
        return appointmentsLayoutBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedDocuList = ViewModelProvider(requireActivity())[SharedDocuList::class.java]
        appointmentsLayoutBinding.lstAppointment.layoutManager = LinearLayoutManager(context)
        sharedDocuList.appoList.observe(viewLifecycleOwner) {
            appointmentsLayoutBinding.lstAppointment.adapter =
                AppointmentAdapter(
                    it.sortedWith(
                        compareByDescending<AppointmentDtl> { e ->
                            if (Build.VERSION.SDK_INT >= 34) {
                                LocalDate.ofInstant(
                                    e.appointmentDate,
                                    ZoneId.systemDefault()
                                )
                            } else {
                                LocalDateTime.ofInstant(e.appointmentDate, ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                    }
                        .thenByDescending { e1 ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                LocalTime.ofInstant(e1.appointmentTime, ZoneId.systemDefault())
                            } else {
                                LocalDateTime.ofInstant(
                                    e1.appointmentTime,
                                    ZoneId.systemDefault()
                                )
                                    .toLocalTime()
                            }
                        }
                ), layoutInflater, appointmentsLayoutBinding.root)
        }
    }
}