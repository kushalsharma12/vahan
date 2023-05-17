package com.example.vahan2

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.vahan2.databinding.FragmentEditProfileBinding
import com.example.vahan2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().reference


        binding.updateBtnEditProfileFrag.setOnClickListener {

            updateUserToDatabase(it)
        }

        binding.cancelBtnEditProfileFrag.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_editProfileFragment_to_navigation_my_profile)

            Toast.makeText(this.context, "Update Canceled", Toast.LENGTH_SHORT).show()

        }

        return root
    }

    private fun updateUserToDatabase(view: View) {
        val uid = auth.currentUser!!.uid
        val newUserName = binding.userNAmeditProfile.text.toString()
        val newEmail = binding.userMailEditProfileFrag.text.toString()
        val newphno = binding.phoneEditProfileFrag.text.toString()
        val newAddress = binding.adressEditProfileFrag.text.toString()

        val newVModelNo = binding.vehicleModelNoEditProfileFrag.text.toString()
        val newRegYr = binding.vehicleRegYearEditProfileFrag.text.toString()
        val newVName = binding.vehicleNameEditProfileFrag.text.toString()
        val newRto = binding.rtoNoEditProfileFrag.text.toString()
        val newDisTrav = binding.distanceTravelledEditProfileFrag.text.toString()

        if(!TextUtils.isEmpty(newUserName) &&  !TextUtils.isEmpty(newEmail)
            && !TextUtils.isEmpty(newphno) &&  !TextUtils.isEmpty(newAddress)
            && !TextUtils.isEmpty(newVModelNo) &&  !TextUtils.isEmpty(newRegYr)
            && !TextUtils.isEmpty(newVName) &&  !TextUtils.isEmpty(newRto)
            && !TextUtils.isEmpty(newDisTrav)
        ){
            dbRef.child("user").child(uid).setValue(
                User(
                    uid, newUserName, newEmail, newphno, newAddress,
                    newVModelNo, newRegYr, newVName, newRto, newDisTrav
                )
            )
            Navigation.findNavController(view)
                .navigate(R.id.action_editProfileFragment_to_navigation_my_profile)

            Toast.makeText(this.context, "Profile Updated", Toast.LENGTH_SHORT).show()

        }
        else{
            Toast.makeText(this.context, "Empty Field are not allowed.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

