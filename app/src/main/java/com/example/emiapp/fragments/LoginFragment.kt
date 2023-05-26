package com.example.emiapp.fragments

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.emiapp.R
import com.example.emiapp.models.FirebaseAuthWrapper
import com.example.emiapp.models.FragmentNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var forgotPass : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_login, container, false)
        username = view.findViewById(R.id.LoginUserEmail)
        password = view.findViewById(R.id.LoginUserPassword)
        auth = Firebase.auth
        firebaseAuth = FirebaseAuth.getInstance()
        forgotPass = view.findViewById(R.id.forgotPass)

        view.findViewById<TextView>(R.id.GoToReg).setOnClickListener{
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(RegisterFragment(),false)
        }

        view.findViewById<Button>(R.id.LoginButton).setOnClickListener {
            validateForm()
        }

        forgotPass.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val username = view.findViewById<EditText>(R.id.editBox)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(username)
                dialog.dismiss()
            }

            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            dialog.show()
        }

        return view
    }

    private fun compareEmail(username: EditText) {
        if(username.text.toString().isEmpty()) {
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return
        }

        auth.sendPasswordResetEmail(username.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, getString(R.string.checkemail), Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context,task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm() {
        val icon = AppCompatResources.getDrawable(requireContext(),R.drawable.alert)
        icon?.setBounds(0,0,icon.intrinsicWidth,icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(username.text.toString().trim())-> {
                username.setError( getString(R.string.insertEmail))
            }

            TextUtils.isEmpty(password.text.toString().trim())-> {
                password.setError( getString(R.string.insertPassword))
            }

            username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() -> {
                if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    firebaseSignIn()
                }
                else {
                    username.setError( getString(R.string.insertValidEmail))
                }
            }
        }
    }

    private fun firebaseSignIn() {
        auth.signInWithEmailAndPassword(username.text.toString(),password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, getString(R.string.successLogin), Toast.LENGTH_SHORT).show()
                    auth.currentUser
                    val firebaseAuthWrapper = FirebaseAuthWrapper(this.requireContext())
                    firebaseAuthWrapper.logSuccess()
                } else {

                    Toast.makeText(context,task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}