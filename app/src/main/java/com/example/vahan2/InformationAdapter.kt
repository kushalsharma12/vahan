package com.example.vahan2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.vahan2.models.info
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class InformationAdapter(options: FirestoreRecyclerOptions<info>, val listner: iInfoAdapter) :
    FirestoreRecyclerAdapter<info, InformationAdapter.InformationViewHolder>(options) {

    class InformationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val infoName: TextView = itemView.findViewById(R.id.infoItemName)
        val infoDescp: TextView = itemView.findViewById(R.id.infoItemDescp)
        val infoPic: ImageView = itemView.findViewById(R.id.infoImage)
        val infoPos: TextView = itemView.findViewById(R.id.infoItemPosition)
        val infoItemPrice: TextView = itemView.findViewById(R.id.infoItemPrice)
        val infoContainer: CardView = itemView.findViewById(R.id.infoContainer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationViewHolder {

        val viewHolder = InformationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_information, parent, false)
        )

        return viewHolder
    }

    override fun onBindViewHolder(holder: InformationViewHolder, position: Int, model: info) {

        holder.infoName.text = model.itemName
        holder.infoDescp.text = model.itemDescription
        holder.infoItemPrice.text = buildString {
            append("Price- ")
            append(model.itemPricing)
        }
        holder.infoPos.text = buildString {
            append("Position- ")
            append(model.itemPosition)
        }
        holder.infoContainer.setOnClickListener {
            listner.onContainerClicked(
                it,
                model.itemName,
                model.itemDescription,
                model.itemPricing,
                model.itemPosition,
                model.itemImage
            )
        }

        Glide.with(holder.infoPic.context).load(model.itemImage)
            .transform(CenterCrop(), RoundedCorners(12))
            .into(holder.infoPic)
    }

}

interface iInfoAdapter {
    fun onContainerClicked(
        view: View,
        itemName: String,
        itemDescription: String,
        itemPricing: String,
        itemPosition: String,
        itemImage: String
    )
}