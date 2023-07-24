package com.docapp.frnt.data.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedDocuList : ViewModel() {
    val docuList = MutableLiveData<List<FileInfo>>()
    val appoList = MutableLiveData<List<AppointmentDtl>>()
}