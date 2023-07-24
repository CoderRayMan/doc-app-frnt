package com.docapp.frnt.ui.doctorTabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.docapp.frnt.SecondFragment
import com.docapp.frnt.data.model.DoctorDetails

class DoctorTabsPagerAdapter(
    private val tabList: List<String>,
    private val doctorDetails: DoctorDetails,
    private var fragment: SecondFragment,
) : FragmentStateAdapter(fragment.requireActivity()) {
    private lateinit var currentFragment: Fragment
    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        currentFragment =
            this.fragment.requireActivity().supportFragmentManager.fragments[position] // set currentFragment when a new fragment is selected
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> BookingFragment(doctorDetails, this.fragment)
            1 -> PrescriptionFragment()
            else -> AppointmentsFragment()
        }


        fragment.arguments = this.fragment.requireArguments()
        return fragment
    }
}