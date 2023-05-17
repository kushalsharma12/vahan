package com.example.vahan2

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.vahan2.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth

        binding.logInButtonLoginFrag.setOnClickListener {
            logInUser(it)
        }

        return root
    }

    private fun logInUser(view: View) {

        binding.loginProgressBar.visibility = View.VISIBLE

        val loginMail = binding.loginNameETLoginFrag.text.toString()
        val loginPassword = binding.loginPasswordetLoginFrag.text.toString()

        if (TextUtils.isEmpty(loginMail)) {
            Toast.makeText(this.context, "Please enter email...", Toast.LENGTH_LONG).show()
            binding.loginProgressBar.visibility = View.GONE
            return
        }
        if (TextUtils.isEmpty(loginPassword)) {
            Toast.makeText(this.context, "Please enter password!", Toast.LENGTH_LONG).show()
            binding.loginProgressBar.visibility = View.GONE
            return
        }
        auth.signInWithEmailAndPassword(loginMail, loginPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Toast.makeText(this.context, "Welcome Again!", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_navigation_home)

                binding.loginProgressBar.visibility = View.GONE

            } else {
                Toast.makeText(this.context, "Login failed! Please try again later", Toast.LENGTH_LONG).show()
                binding.loginProgressBar.visibility = View.GONE

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

