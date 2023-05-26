package com.example.emiapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.emiapp.R
import com.example.emiapp.fragments.DateFragment
import com.example.emiapp.models.FragmentNavigation

class AddHeadacheActivity : AppCompatActivity() , FragmentNavigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_headache)

        supportFragmentManager.beginTransaction()
            .add(R.id.addContainer, DateFragment())
            .commit()
    }

    override fun navigateFrag(fragment: Fragment, addToStack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.addContainer, fragment)

        if(addToStack){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
