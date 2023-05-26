package com.example.emiapp.models


import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.example.emiapp.R
import com.example.emiapp.fragments.RegisterFragment
import java.util.regex.Matcher
import java.util.regex.Pattern

class PasswordStrengthCalculator : TextWatcher {
    var strengthLevel: MutableLiveData<String> = MutableLiveData()
    var strengthColor: MutableLiveData<Int> = MutableLiveData()
    var lowerCase: MutableLiveData<Int> = MutableLiveData(0)
    var upperCase: MutableLiveData<Int> = MutableLiveData(0)
    var digit: MutableLiveData<Int> = MutableLiveData(0)
    var specialchar: MutableLiveData<Int> = MutableLiveData(0)

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if(char != null) {
            lowerCase.value = if(char.hasLowerCase()) {1} else {0}
            upperCase.value = if(char.hasLowerCase()) {1} else {0}
            digit.value = if(char.hasLowerCase()) {1} else {0}
            specialchar.value = if(char.hasLowerCase()) {1} else {0}
            calculateStrength(char)
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun calculateStrength(password: CharSequence) {

        if (password.length in 0..7) {
            strengthColor.value = R.color.weak
            strengthLevel.value = "PASSWORD DEBOLE"
        } else if (password.length in 8..10) {
            if (lowerCase.value == 1 || upperCase.value == 1 || digit.value == 1 || specialchar.value == 1) {
                strengthColor.value = R.color.medium
                strengthLevel.value = "PASSWORD MEDIA"
            }
        } else if (password.length in 11..16) {
            if (lowerCase.value == 1 || upperCase.value == 1 || digit.value == 1 || specialchar.value == 1) {
                if (lowerCase.value == 1 && upperCase.value == 1) {
                    strengthColor.value = R.color.strong
                    strengthLevel.value = "PASSWORD FORTE"
                }
            } else if (password.length > 16) {
                if (lowerCase.value == 1 && upperCase.value == 1 && digit.value == 1 && specialchar.value == 1) {
                    strengthColor.value = R.color.bulletproof
                    strengthLevel.value = "PASSWORD MOLTO FORTE"
                }
            }
        }
    }

    private fun CharSequence.hasLowerCase(): Boolean {
        val pattern: Pattern = Pattern.compile("[a-z]")
        val hasLowerCase: Matcher = pattern.matcher(this)
        return hasLowerCase.find()
    }

    private fun CharSequence.hasUpperCase(): Boolean {
        val pattern: Pattern = Pattern.compile("[A-Z]")
        val hasUpperCase: Matcher = pattern.matcher(this)
        return hasUpperCase.find()
    }

    private fun CharSequence.hasDigit(): Boolean {
        val pattern: Pattern = Pattern.compile("[0-9]")
        val hasDigit: Matcher = pattern.matcher(this)
        return hasDigit.find()
    }

    private fun CharSequence.hasSpecialChar(): Boolean {
        val pattern: Pattern = Pattern.compile("[!@#$%^&*()_=+-{}/.<>|\\[\\]]")
        val hasSpecialChar: Matcher = pattern.matcher(this)
        return hasSpecialChar.find()
    }
}