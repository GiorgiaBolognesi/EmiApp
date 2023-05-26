package com.example.emiapp.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class DataRepository {
    private val databaseReference: DatabaseReference? =
        Firebase.auth.currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference().child(HEADACHE_NODE).child(
                it
            )
        }
    @Volatile private var INSTANCE : DataRepository ?= null

    //SE L'ISTANZA è NULLA RITORNA UNA NUOVA ISTANZA, ALTRIMENTI SE C'è GIA
    //RITORNA LA STESSA ISTANZA
    fun getInstance() : DataRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = DataRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadData(dataList: MutableLiveData<List<Data>>, st:String) {
        databaseReference?.orderByChild("startDate")?.equalTo(st)?.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _dataList: List<Data> = snapshot.children.map {dataSnapshot ->
                        dataSnapshot.getValue(Data::class.java)!!
                    }
                    dataList.postValue(_dataList)
                }catch (e: Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}