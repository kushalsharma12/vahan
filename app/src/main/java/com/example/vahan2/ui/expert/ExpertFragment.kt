package com.example.vahan2.ui.expert

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.vahan2.ExpertAdapter
import com.example.vahan2.R
import com.example.vahan2.databinding.FragmentExpertBinding
import com.example.vahan2.iExpertAdapter
import com.example.vahan2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ExpertFragment : Fragment(), iExpertAdapter {

    private var _binding: FragmentExpertBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExpertAdapter
    private lateinit var expertList: ArrayList<User>

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExpertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().reference

        expertList = ArrayList()
        adapter = ExpertAdapter(this.requireContext(), expertList, this)

        binding.expertRecylerView.adapter = adapter

        binding.expertProgressBar.visibility = View.VISIBLE


        dbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                expertList.clear()

                // getting current user isExpert status.
                val isExpert = snapshot.child(auth.currentUser!!.uid).child("isExpert").value
                Log.e("isExpert", isExpert.toString())

                for (postSnapshot in snapshot.children) {

                    val currentUser = postSnapshot.getValue(User::class.java)

                    // if currentUser is an Expert then only users will be visible
                    if (isExpert == true) {
                        if (auth.currentUser?.uid != currentUser?.uid && currentUser?.isExpert == false) {
                            expertList.add(currentUser!!)
                        }
                    }
                    // if currentUser is a user then only experts will be visible
                    else {
                        if (auth.currentUser?.uid != currentUser?.uid && currentUser?.isExpert == true) {
                            expertList.add(currentUser!!)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
                binding.expertProgressBar.visibility = View.GONE

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("FirebaseError","from ExpertFragment $error")
                binding.expertProgressBar.visibility = View.GONE
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onConsultBtnClicked(v: View, name: String?, uid: String) {
        val bundle = bundleOf("name" to name, "uid" to uid)
        v.findNavController().navigate(R.id.action_navigation_expert_to_chatFragment, bundle)
    }


}