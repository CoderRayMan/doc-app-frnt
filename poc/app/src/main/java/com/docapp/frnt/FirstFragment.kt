package com.docapp.frnt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.docapp.frnt.data.companions.CommonFunctions.getDocDetailsAll
import com.docapp.frnt.databinding.FragmentFirstBinding
import com.docapp.frnt.ui.doctorCards.DoctorsListViewAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var _intent: Intent
    private lateinit var userInfo: Bundle
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        _intent = requireActivity().intent
        userInfo = _intent.extras!!
        val result = getDocDetailsAll(requireContext())
        binding.allDoctorCards.layoutManager = LinearLayoutManager(context)
        if (result != null) {
            binding.allDoctorCards.adapter = DoctorsListViewAdapter(
                inflater,
                findNavController(),
                container,
                result.doctorDetailsList
            )
        }
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}