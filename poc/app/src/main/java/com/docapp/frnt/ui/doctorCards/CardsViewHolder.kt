package com.docapp.frnt.ui.doctorCards

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.docapp.frnt.R
import com.docapp.frnt.data.companions.Constants.DOC_ID
import com.docapp.frnt.data.companions.Constants.DOC_LOC
import com.docapp.frnt.data.companions.Constants.DOC_NAME
import com.docapp.frnt.data.companions.Constants.DOC_SPEC
import com.docapp.frnt.data.model.DoctorDetails
import com.docapp.frnt.databinding.DoctorCardsBinding


class CardsViewHolder(
    private var cardView: DoctorCardsBinding,
    private val navController: NavController,
) : ViewHolder(cardView.root) {

    fun setDetails(doctorDetails: DoctorDetails) {
        cardView.valDocName.text = doctorDetails.dctName
        cardView.valDocSpec.text = doctorDetails.specialisation
        cardView.valDocLocation.text = doctorDetails.location
        cardView.valDocExp.text =
            if (doctorDetails.yrsOfExp == null) "0" else doctorDetails.yrsOfExp.toString()
        cardView.bookAnAppo.setOnClickListener {
            navigateToBookingScreen(doctorDetails)

        }
    }

    private fun navigateToBookingScreen(doctorDetails: DoctorDetails) {
        navController.graph.findNode(R.id.SecondFragment)?.label = doctorDetails.dctName
        navController.navigate(
            R.id.action_FirstFragment_to_SecondFragment, bundleOf(
                DOC_ID to doctorDetails.dctId,
                DOC_NAME to doctorDetails.dctName,
                DOC_SPEC to doctorDetails.specialisation,
                DOC_LOC to doctorDetails.location
            )
        )
    }
}