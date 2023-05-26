package com.example.emiapp.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class ViewModel : ViewModel(){

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    //PER CALENDARACT
    private lateinit var repository: DataRepository
    private val _allData = MutableLiveData<List<Data>>()
    val allData : LiveData<List<Data>> = _allData

    fun add(data: String) {
        repository = DataRepository().getInstance()
        repository.loadData(_allData, data)
        Log.d("MMMM", data)
    }

    fun addData(data: Data) {
        val user:String? = Firebase.auth.currentUser?.uid
        val timeStampPref: DatabaseReference = FirebaseDatabase.getInstance().getReference(HEADACHE_NODE)
        timeStampPref.child(user!!).push().setValue(data).addOnCompleteListener {
            if(it.isSuccessful) {
                _result.value = null
            }
            else  {
                _result.value = it.exception
            }
        }
    }
}