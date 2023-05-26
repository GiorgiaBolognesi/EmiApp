package com.example.emiapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.emiapp.R
import com.example.emiapp.models.FirebaseAuthWrapper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Check if logged or not
        val firebaseWrapper = FirebaseAuthWrapper(this)
        if (!firebaseWrapper.isAuthenticated()) {
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        //Start main activity
        else {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }
}