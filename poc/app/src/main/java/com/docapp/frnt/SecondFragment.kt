package com.docapp.frnt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.docapp.frnt.data.companions.CommonFunctions.apiCallGetDocuDetails
import com.docapp.frnt.data.companions.CommonFunctions.apiCallGetPresDetails
import com.docapp.frnt.data.companions.CommonFunctions.getDocDetails
import com.docapp.frnt.data.companions.CommonFunctions.getSerializable
import com.docapp.frnt.data.companions.Constants.DOC_ID
import com.docapp.frnt.data.companions.Constants.UUID
import com.docapp.frnt.data.model.AppointmentDtl
import com.docapp.frnt.data.model.DoctorDetails
import com.docapp.frnt.data.model.FileInfo
import com.docapp.frnt.data.model.SharedDocuList
import com.docapp.frnt.databinding.FragmentSecondBinding
import com.docapp.frnt.ui.doctorTabs.DoctorTabsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import java.math.BigInteger


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var _intent: Intent
    private lateinit var userInfo: Bundle
    private lateinit var doctorDetails: DoctorDetails
    private val tabs = listOf("Slots", "Documents", "Appointments")
    private lateinit var docuList: List<FileInfo>
    private lateinit var appoList: List<AppointmentDtl>
    private lateinit var userId: BigInteger
    private lateinit var sharedDocuList: SharedDocuList

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        _intent = requireActivity().intent
        userInfo = _intent.extras!!
        doctorDetails =
            getDocDetails(
                this.requireContext(),
                getSerializable(requireArguments(), DOC_ID, BigInteger::class.java)!!
            )!!
        userId = getSerializable(userInfo, UUID, BigInteger::class.java)!!
        binding.pager.isUserInputEnabled = false
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedDocuList = ViewModelProvider(requireActivity())[SharedDocuList::class.java]
        docuList =
            apiCallGetDocuDetails(requireContext(), doctorDetails.dctId, userId)
        sharedDocuList.docuList.value = docuList
        appoList = apiCallGetPresDetails(requireContext(), doctorDetails.dctId, userId)
        sharedDocuList.appoList.value = appoList
        binding.pager.adapter = DoctorTabsPagerAdapter(
            tabs,
            doctorDetails = doctorDetails, this,
        )
        TabLayoutMediator(binding.tabView, binding.pager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun refreshPatientDetails(docuList: List<FileInfo>?, appoList: List<AppointmentDtl>?) {
        sharedDocuList.docuList.value = docuList
        sharedDocuList.appoList.value = appoList

    }

}