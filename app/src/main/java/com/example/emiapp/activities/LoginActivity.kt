package com.example.emiapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.emiapp.R
import com.example.emiapp.fragments.LoginFragment
import com.example.emiapp.models.FragmentNavigation

class LoginActivity : AppCompatActivity(), FragmentNavigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, LoginFragment())
            .commit()
    }

    override fun navigateFrag(fragment: Fragment, addToStack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if(addToStack){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}