package com.example.vahan2.ui.myProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.vahan2.R
import com.example.vahan2.databinding.FragmentMyProfileBinding
import com.example.vahan2.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ValueEventListener

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        val uid = auth.currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference.child("user").child(uid)

        dbRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.getValue(User::class.java)
                addUserDatatoView(user)
            } else {
                Log.d("myProfileError", "${it.exception?.message}")
            }
        }


        binding.editBtnMYProfileFrag.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_my_profile_to_editProfileFragment)
        }

        binding.myProfileLogoutBtn.setOnClickListener {
            auth.signOut()
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_my_profile_to_initialFragment)
            finishAffinity(this.requireActivity())
        }

        return root
    }

    private fun addUserDatatoView(user: User?) {

        dbRef.child("user").child(auth.currentUser!!.uid).child("isExpert").get()
            .addOnSuccessListener {
                if (it.value == true) {
                    // expert has logged in so expert profile is visible.
                    binding.myProfileVehicleInfoContainer.visibility = View.GONE
                    binding.editBtnMYProfileFrag.visibility = View.GONE
                    binding.myProfileIvContainer.visibility = View.VISIBLE
                    binding.mailProfileFrag.text = user!!.expertOccup
                    binding.phNoProfileFrag.text = user.expertLang
                    binding.myProfileExpTv.text = user.expertExp
                    Glide.with(binding.myProfileIv.context).load(user.expertImage)
                        .transform(CenterCrop(), RoundedCorners(12))
                        .into(binding.myProfileIv)

                } else {
                    // user had logged in and user profile is visible.
                    binding.myProfileIvContainer.visibility = View.GONE
                    binding.myProfileVehicleInfoContainer.visibility = View.VISIBLE
                    binding.editBtnMYProfileFrag.visibility = View.VISIBLE

                    //user info
                    binding.userNameProfileFrag.text = user!!.name.toString()
                    binding.mailProfileFrag.text = user.mail.toString()
                    binding.phNoProfileFrag.text = user.phoneNo.toString()
                    binding.adressProfileFrag.text = user.address.toString()

                    //vehicle info
                    binding.vehicleModelNoProfileFrag.text = user.vehicleModleNo
                    binding.vehicleRegYearProfileFrag.text = user.vehicleRegYear
                    binding.vehicleNameProfileFrag.text = user.vehicleName
                    binding.rtoNoProfileFrag.text = user.rtoNo
                    binding.distanceTravelledProfileFrag.text = user.distanceTraveled


                }


            }.addOnFailureListener {
                Log.e("MyProfile", "Error in fetcing user or isExper", it)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}