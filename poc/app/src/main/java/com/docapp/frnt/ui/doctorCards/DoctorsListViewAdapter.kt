package com.docapp.frnt.ui.doctorCards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.docapp.frnt.data.model.DoctorDetails
import com.docapp.frnt.databinding.DoctorCardsBinding

class DoctorsListViewAdapter(
    private var layoutInflater: LayoutInflater,
    private val navController: NavController,
    private val viewGroup: ViewGroup?,
    private val docDetailsList: List<DoctorDetails>
) : RecyclerView.Adapter<CardsViewHolder>() {
    private lateinit var cardView: DoctorCardsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        cardView = DoctorCardsBinding.inflate(layoutInflater, viewGroup, false)
        return CardsViewHolder(cardView, navController)
    }

    override fun getItemCount(): Int {
        return docDetailsList.size
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        holder.setDetails(docDetailsList[position])
    }
}