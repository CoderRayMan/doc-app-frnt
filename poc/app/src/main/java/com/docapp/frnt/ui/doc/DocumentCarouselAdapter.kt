package com.docapp.frnt.ui.doc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.docapp.frnt.data.model.FileInfo
import com.docapp.frnt.databinding.CardDocumentItemBinding

class DocumentCarouselAdapter(
    private val documentList: List<FileInfo>,
    private var layoutInflater: LayoutInflater,
    private val viewGroup: ViewGroup?
) : Adapter<DocumentCardItem>() {
    private lateinit var cardBinding: CardDocumentItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentCardItem {
        cardBinding = CardDocumentItemBinding.inflate(layoutInflater, viewGroup, false)
        return DocumentCardItem(cardBinding)
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    override fun onBindViewHolder(holder: DocumentCardItem, position: Int) {
        holder.setDetails(documentList[position])
    }
}