package com.example.vahan2

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vahan2.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.initialFragment -> {
                    navView.visibility = View.GONE
                }

                R.id.loginFragment -> {
                    navView.visibility = View.GONE
                }

                R.id.signInFragment -> {
                    navView.visibility = View.GONE
                }

                R.id.editProfileFragment -> {
                    navView.visibility = View.GONE
                }

                R.id.chatFragment -> {
                    navView.visibility = View.GONE
                }
                R.id.infoClcickedFragment ->{
                    navView.visibility = View.GONE
                }

                else -> {
                    navView.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate(R.id.navigation_expert)
        }
        else{
            navController.navigate(R.id.initialFragment)
        }
    }
}

// TODO- Back stack of profile fragment is not done till yet + back stack of expert fragment is also not done.


// todo- change test mode date of firebase in storeage, realtme data base etc.

// todo- convert realtime db in signup frag to dao

