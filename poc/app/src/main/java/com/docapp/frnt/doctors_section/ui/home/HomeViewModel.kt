package com.docapp.frnt.doctors_section.ui.home
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.docapp.frnt.data.companions.DoctorProfile
import com.docapp.frnt.data.model.AppointmentDtl

class HomeViewModel : ViewModel() {
    val profileDetails = MutableLiveData<DoctorProfile>()
    val appoList = MutableLiveData<List<AppointmentDtl>>()
}