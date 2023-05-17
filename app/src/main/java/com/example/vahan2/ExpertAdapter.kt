package com.example.vahan2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.vahan2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class ExpertAdapter(
    val context: Context,
    val expertList: ArrayList<User>,
    val listner: iExpertAdapter
) :
    RecyclerView.Adapter<ExpertAdapter.ExpertViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    class ExpertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val expertName: TextView = itemView.findViewById(R.id.expertNameTv)
        val expertLang: TextView = itemView.findViewById(R.id.expertLangTv)
        val expertOccup: TextView = itemView.findViewById(R.id.expertOccupTv)

        val expertProfilePic: ImageView = itemView.findViewById(R.id.expertProfilePicImageView)
        val expertConsultNowButton: Button = itemView.findViewById(R.id.consultNowBtn)
        val expertExp: TextView = itemView.findViewById(R.id.expertExpTv)
        val expertImgExpContainer : CardView = itemView.findViewById(R.id.expertImagExpContainer)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.item_experts, parent, false)
        return ExpertViewHolder(view)

    }

    override fun getItemCount(): Int {
        return expertList.size
    }

    override fun onBindViewHolder(holder: ExpertViewHolder, position: Int) {
        val currentExpert = expertList[position]
        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().reference

        holder.expertName.text = currentExpert.name

        holder.expertConsultNowButton.setOnClickListener {
            listner.onConsultBtnClicked(it, currentExpert.name, currentExpert.uid)
        }

        dbRef.child("user").child(auth.currentUser!!.uid).child("isExpert").get()
            .addOnSuccessListener {
                if (it.value == true) {
                    // expert has logged in and user are visible
                    holder.expertImgExpContainer.visibility = View.GONE
                    holder.expertConsultNowButton.text = context.getString(R.string.chat_with_user)
                    holder.expertLang.text = currentExpert.phoneNo
                    holder.expertOccup.text = currentExpert.mail

                } else {
                    // user had logged in and experts are visible
                    holder.expertImgExpContainer.visibility = View.VISIBLE
                    Glide.with(holder.expertProfilePic.context).load(currentExpert.expertImage)
                        .transform(CenterCrop(), RoundedCorners(12))
                        .into(holder.expertProfilePic)
                    holder.expertLang.text = currentExpert.expertLang
                    holder.expertOccup.text = currentExpert.expertOccup
                    holder.expertExp.text = currentExpert.expertExp

                    holder.expertConsultNowButton.text = context.getString(R.string.consult_now)

                }

            }.addOnFailureListener {
                Log.i("ExpertFragment", "Error getting data", it)
            }
    }

}

interface iExpertAdapter {
    fun onConsultBtnClicked(v: View, name: String?, uid: String)

}
