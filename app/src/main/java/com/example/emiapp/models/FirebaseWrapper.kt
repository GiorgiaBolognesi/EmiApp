package com.example.emiapp.models

import android.content.Context
import android.content.Intent
import com.example.emiapp.activities.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthWrapper(private val context: Context?){
    private var auth: FirebaseAuth = Firebase.auth

    fun isAuthenticated() : Boolean {
        return auth.currentUser != null
    }

    fun logSuccess() {
        val intent = Intent(this.context, SplashActivity::class.java)
        context?.startActivity(intent)
    }

}
