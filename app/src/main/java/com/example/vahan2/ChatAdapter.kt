package com.example.vahan2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vahan2.models.Message
import com.example.vahan2.utils.Utils
import com.google.firebase.auth.FirebaseAuth


class ChatAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            // inflate receive
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_recieve, parent, false)
            return ReceivedViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            // you are sending a message to other user.
            return ITEM_SENT
        } else {
            // you are not logged in and other user is sending a message to you.
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        val timeStamp = messageList[position].createdAt

        if (holder.javaClass == SentViewHolder::class.java) {
            // for sent
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
            holder.sentTimeStamp.text = Utils.getTimeAgo(timeStamp)

        } else {
            // for received
            val viewHolder = holder as ReceivedViewHolder
            holder.receivedMessage.text = currentMessage.message
            holder.receivedTimeStamp.text = Utils.getTimeAgo(timeStamp)


        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sentMessage: TextView = itemView.findViewById(R.id.messageSentTv)
        val sentTimeStamp : TextView = itemView.findViewById(R.id.timestamp_sent_tv)

    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage: TextView = itemView.findViewById(R.id.messageRecieveTv)
        val receivedTimeStamp : TextView = itemView.findViewById(R.id.timeStamp_recieve_tv)


    }

}