package com.docapp.frnt.ui.doctorTabs

import android.content.res.Resources.getSystem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.docapp.frnt.data.companions.CommonFunctions.getSerializableExtra
import com.docapp.frnt.data.companions.Constants.UUID
import com.docapp.frnt.data.model.SharedDocuList
import com.docapp.frnt.databinding.FragmentPrescriptionBinding
import com.docapp.frnt.ui.doc.DocumentCarouselAdapter
import java.math.BigInteger

class PrescriptionFragment : Fragment() {
    private var _binding: FragmentPrescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var uuid: BigInteger
    private lateinit var sharedDocuList: SharedDocuList
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrescriptionBinding.inflate(inflater, container, false)
        uuid = getSerializableExtra(requireActivity().intent, UUID, BigInteger::class.java)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedDocuList = ViewModelProvider(requireActivity())[SharedDocuList::class.java]
        binding.pagerDocCards.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }
        binding.pagerDocuPrescriptions.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * getSystem().displayMetrics.density).toInt()))
        binding.pagerDocCards.setPageTransformer(compositePageTransformer)
        binding.pagerDocuPrescriptions.setPageTransformer(compositePageTransformer)
        sharedDocuList.docuList.observe(viewLifecycleOwner) {
            binding.pagerDocCards.adapter =
                DocumentCarouselAdapter(
                    it.filterNot { e -> e.isPrescription },
                    layoutInflater,
                    binding.pagerDocCards
                )
            binding.pagerDocuPrescriptions.adapter =
                DocumentCarouselAdapter(
                    it.filter { e -> e.isPrescription },
                    layoutInflater,
                    binding.pagerDocCards
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}