package com.example.emiapp.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.emiapp.R
import com.example.emiapp.models.FirebaseAuthWrapper
import com.example.emiapp.models.FragmentNavigation
import com.example.emiapp.models.PasswordStrengthCalculator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var passwordStength: TextView
    private lateinit var auth : FirebaseAuth
    private var color: Int = R.color.weak

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_register, container, false)

        username = view.findViewById(R.id.RegUserEmail)
        password = view.findViewById(R.id.RegUserPassword)
        passwordStength = view.findViewById(R.id.PasswordStength)
        auth = Firebase.auth
        val passwordStrengthCalculator = PasswordStrengthCalculator()

        //observers
        passwordStrengthCalculator.strengthLevel.observe(viewLifecycleOwner, Observer { strengthLevel ->
            displayStrengthLevel(strengthLevel)
        })

        passwordStrengthCalculator.strengthColor.observe(viewLifecycleOwner,
            Observer { strengthColor->
                color = strengthColor
            })

        view.findViewById<TextView>(R.id.GoToLog).setOnClickListener{
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(LoginFragment(),false)
        }

        view.findViewById<Button>(R.id.Register).setOnClickListener {
            validateEmptyForm()
        }

        password.addTextChangedListener(passwordStrengthCalculator)

        return view
    }

    private fun displayStrengthLevel(strengthLevel: String?) {
        passwordStength.text = strengthLevel
        passwordStength.setTextColor(ContextCompat.getColor(requireActivity(),color))

    }

    private fun validateEmptyForm() {
        val icon = AppCompatResources.getDrawable(requireContext(),R.drawable.alert)
        icon?.setBounds(0,0,icon.intrinsicWidth,icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(username.text.toString().trim())-> {
                username.setError("Please enter email")
            }

            TextUtils.isEmpty(password.text.toString().trim())-> {
                password.setError("Please enter password")
            }

            username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() -> {
                if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    if(password.text.toString().length >= 8) {
                        firebaseSignUp()
                    }
                    else {
                        password.setError("Please enter at least 8 character")
                    }
                }
                else {
                    username.setError("Please enter valid email")
                }
            }
        }
    }

    private fun firebaseSignUp(){
        auth.createUserWithEmailAndPassword(username.text.toString(),password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context,"Register succesful", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    val firebaseAuthWrapper : FirebaseAuthWrapper = FirebaseAuthWrapper(this.requireContext())
                    firebaseAuthWrapper.logSuccess()
                }
                else {
                    Toast.makeText(context,task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}