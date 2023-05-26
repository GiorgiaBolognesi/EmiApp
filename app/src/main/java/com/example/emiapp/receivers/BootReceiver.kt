package com.example.emiapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.emiapp.models.runInstantWorker
import com.example.emiapp.models.startPeriodicWorker

class BootReceiver : BroadcastReceiver(){
    private val TAG : String = BootReceiver::class.simpleName.toString()
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Start service for weather informations")
        runInstantWorker(context)
        startPeriodicWorker(context)
    }
}