package com.example.vahan2

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.vahan2.databinding.FragmentVhpsBinding
import com.example.vahan2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class VhpsFragment : Fragment() {

    private var _binding: FragmentVhpsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVhpsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth

        val uid = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("user").child(uid)

        dbRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.getValue(User::class.java)
                binding.distanceTravVhpsEt.setText(user!!.distanceTraveled)
            } else {
                Log.d("vhpsError", "${it.exception?.message}")
            }
        }

        binding.showVhpsBtn.setOnClickListener {
            val distance = binding.distanceTravVhpsEt.text.toString().toIntOrNull() ?: 0
            val serviceCount = binding.serviceCountEt.text.toString().toIntOrNull() ?: 0

            if (distance <= 10000 && serviceCount < 3) {
                carHealth("Poor")
            } else if (distance <= 10000 && serviceCount == 3) {
                carHealth("Stable")
            } else if (distance <= 10000 && serviceCount > 3) {
                carHealth("Best")

            } else {
                val servicesPer10k =
                    serviceCount / (distance / 10000) // calculate services per 10k kms
                val health: String = when {
                    distance > 613554 -> "Your vehicle has travelled so long, now it's time to buy a new one."
                    servicesPer10k < 3 -> "Poor"
                    servicesPer10k == 3 -> "Stable"
                    servicesPer10k > 3 -> "Best"
                    else -> "Unknown" // handle any unexpected input
                }
                carHealth(health)
            }

        }

        return root
    }



    fun carHealth(health: String) {
        binding.carHealthTv.setText("Car Health: $health")
        binding.carHealthanimVHps.visibility = View.VISIBLE

        if (health == "Poor") {
            binding.carHealthInfoTv.setText("Your car health is poor. You should immediately consult with our experts. ")
        } else if (health == "Stable") {
            binding.carHealthInfoTv.setText("Your car health is stable. It means you are doing regular services at right time.")
        } else {
            binding.carHealthInfoTv.setText("Your car is in best health. You are following best recommended maintenance schedules.")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}