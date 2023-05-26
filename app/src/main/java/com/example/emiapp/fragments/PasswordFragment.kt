package com.example.emiapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.emiapp.R
import com.example.emiapp.activities.MainActivity
import com.example.emiapp.models.FragmentNavigation
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class PasswordFragment : Fragment() {

    lateinit var button: Button
    lateinit var oldPass : EditText
    lateinit var newPass : EditText
    lateinit var confPass : EditText
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_password, container, false)
        button = view.findViewById(R.id.button)
        oldPass = view.findViewById(R.id.vecchiaPass1)
        newPass = view.findViewById(R.id.nuovaPass1)
        confPass = view.findViewById(R.id.confPass1)
        auth = FirebaseAuth.getInstance()

        button.setOnClickListener {
            changePassword()
        }

        return view
    }

    private fun changePassword() {
        if(oldPass.text.toString().isNotEmpty() && newPass.text.toString().isNotEmpty() && confPass.text.toString().isNotEmpty()) {
            if (newPass.text.toString().equals(confPass.text.toString())) {
                if (newPass.text.toString().length >= 8) {
                    val user = auth.currentUser
                    if (user != null && user.email != null) {
                        val credential = EmailAuthProvider
                            .getCredential(user.email!!, oldPass.text.toString())
                        user.reauthenticate(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    user!!.updatePassword(newPass.text.toString())
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    getString(R.string.passwordChanged),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                auth.signOut()
                                                var navPass = activity as FragmentNavigation
                                                navPass.navigateFrag(LoginFragment(), false)

                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.reauthFailed),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    } else {
                        var navPass = activity as FragmentNavigation
                        navPass.navigateFrag(LoginFragment(), false)
                    }

                } else {
                    Toast.makeText(context, getString(R.string.eightChar), Toast.LENGTH_SHORT).show()
                }
            }  else {
                Toast.makeText(context, getString(R.string.passwNotEqual), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, getString(R.string.compileAll), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}