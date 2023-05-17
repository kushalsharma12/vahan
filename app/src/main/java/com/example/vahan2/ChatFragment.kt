package com.example.vahan2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.vahan2.databinding.FragmentChatBinding
import com.example.vahan2.models.Message

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChatAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var dbRef: DatabaseReference

    //These rooms helps to make messages private so that those messages will not reflect in each and every chat.
    // a unique room for the pair of sender and receiver.
    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val receiverUid = arguments?.getString("uid")
        val name = arguments?.getString("name")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        dbRef = FirebaseDatabase.getInstance().reference

        messageList = ArrayList()
        adapter = ChatAdapter(this.requireContext(), messageList)

        //setting adapter
        binding.chatRecyclerView.adapter = adapter

        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    // helps in not repeating the value again and again.
                    messageList.clear()

                    //using loop to throw all the messages in the snapshot
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {


                }

            })

        binding.chattingUserTv.text = name.toString()

        binding.chatBackButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_chatFragment_to_navigation_expert)
        }

        //adding messages to the database
        binding.sendButton.setOnClickListener {
            val message = binding.messageInputEt.text.toString()
            val messageObject = Message(message, senderUid!!,System.currentTimeMillis())

            // with.push() a unique node will always be created.
            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    // at the same we want to update the UI of receiver as well
                    dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            binding.messageInputEt.setText("")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

