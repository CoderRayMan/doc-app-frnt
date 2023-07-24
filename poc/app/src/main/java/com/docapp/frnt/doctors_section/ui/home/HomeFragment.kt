package com.docapp.frnt.doctors_section.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.docapp.frnt.data.companions.CommonFunctions.getSerializable
import com.docapp.frnt.data.companions.Constants
import com.docapp.frnt.data.companions.DoctorProfile
import com.docapp.frnt.data.model.AppointmentDtl
import com.docapp.frnt.databinding.DoctorFragmentHomeBinding
import com.docapp.frnt.doctors_section.appo.AppointmentsAdapter
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class HomeFragment : Fragment() {

    private var _binding: DoctorFragmentHomeBinding? = null
    private lateinit var _intent: Intent
    private lateinit var userInfo: Bundle
    private lateinit var userId: BigInteger
    private lateinit var sharedDetails: HomeViewModel
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var appointments: List<AppointmentDtl>
    private lateinit var profile: DoctorProfile

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DoctorFragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _intent = requireActivity().intent
        userInfo = _intent.extras!!
        userId = getSerializable(userInfo, Constants.UUID, BigInteger::class.java)!!

        return root
    }

    private fun setupRecyclerView() {
        // Set up the RecyclerView with a LinearLayoutManager
        binding.lstAppointmentDoc.layoutManager = LinearLayoutManager(context)

        // Create the AppointmentsAdapter and attach it to the RecyclerView
        appointmentsAdapter = AppointmentsAdapter(appointments.sortedWith(
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
        ))
        binding.lstAppointmentDoc.adapter = appointmentsAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedDetails =
            ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        sharedDetails.profileDetails.observe(viewLifecycleOwner) {
            profile = it
        }
        sharedDetails.appoList.observe(viewLifecycleOwner) {
            appointments = it
            setupRecyclerView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}