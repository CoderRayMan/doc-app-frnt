package com.docapp.frnt.ui.doc

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.docapp.frnt.data.companions.Constants
import com.docapp.frnt.data.model.FileInfo
import com.docapp.frnt.databinding.CardDocumentItemBinding
import java.text.SimpleDateFormat
import java.util.*

class DocumentCardItem(private var cardView: CardDocumentItemBinding) : ViewHolder(cardView.root) {
    fun setDetails(documentDtl: FileInfo) {
        cardView.txtFileName.text = documentDtl.filename
        cardView.textFileUploadDt.text = SimpleDateFormat(
            Constants.DATE_DAY_DATE_DD_MM_YYYY,
            Locale.US
        ).format(Date.from(documentDtl.timestamp))
        cardView.txtFileAppoId.text = "${documentDtl.appointmentId}"
        cardView.txtFileSize.text = documentDtl.fileSize
    }

}