package com.example.vahan2

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.vahan2.databinding.FragmentSignInBinding
import com.example.vahan2.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        dbRef = FirebaseDatabase.getInstance().reference

        binding.SignInButtonSignFrag.setOnClickListener {
            createNewUser(it)
        }

        return root
    }

    @SuppressLint("SuspiciousIndentation")
    private fun createNewUser(view: View) {

        binding.signInProgressBar.visibility = View.VISIBLE

        val mail = binding.signInMailETSignInFrag.text.toString()
        val name = binding.userNameSignFrag.text.toString()
        val password = binding.SignInPasswordetSignInFrag.text.toString()
        val confirmPassword = binding.SignInCnfrmPasswordetSignInFrag.text.toString()
        val phNo = binding.phNoSignFrag.text.toString()
        val address = binding.addressSignInFrag.text.toString()

        val vModelNo = binding.vModelNoEt.text.toString()
        val vRegYear = binding.vRegYearEt.text.toString()
        val vName = binding.vNameEt.text.toString()
        val vRtoNo = binding.vRtoNoEt.text.toString()
        val vDisTrav = binding.vDisnTraveledEt.text.toString()

            auth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {

                        if (password == confirmPassword) {

                            //adding user to realtime database.
                            addUserToDatabase(auth.currentUser?.uid!!, name, mail, phNo, address,
                                vModelNo, vRegYear, vName, vRtoNo, vDisTrav
                            )

                            Toast.makeText(this.context, "Your Welcome", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view)
                                .navigate(R.id.action_signInFragment_to_navigation_home)

                            binding.signInProgressBar.visibility = View.GONE

                        } else {
                            Toast.makeText(this.context, "Confirm password not matched", Toast.LENGTH_SHORT).show()
                            binding.signInProgressBar.visibility = View.GONE
                        }


                    } else {
                        Toast.makeText(this.context, "Registration failed! Please try again later", Toast.LENGTH_LONG).show()
                        binding.signInProgressBar.visibility = View.GONE
                    }
                })

    }

    private fun addUserToDatabase(
        uid: String, name: String, mail: String, phNo: String, address: String,
        vModelNo: String, vRegYear: String, vName: String, vRtoNo: String, vDisTrav: String,
    ) {

        dbRef.child("user").child(uid).setValue(
            User(
                uid, name, mail, phNo, address,
                vModelNo, vRegYear, vName, vRtoNo, vDisTrav
            )
        )

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

