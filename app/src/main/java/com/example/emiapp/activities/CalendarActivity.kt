package com.example.emiapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.emiapp.R
import com.example.emiapp.databinding.ActivityCalendarBinding
import com.example.emiapp.fragments.CalendarFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var dateString:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupCalendar()
        val calendarButton = findViewById<FloatingActionButton>(R.id.CalendarAddButton)
        val comebackButton = findViewById<FloatingActionButton>(R.id.comebackButton)

        calendarButton.setOnClickListener {
            val intent = Intent(this, AddHeadacheActivity::class.java)
            this.startActivity(intent)
        }

        comebackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }


    }

    private fun replaceFragment(fragment: Fragment, s:String) {
        val frag = CalendarFragment.newInstance(s)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView,frag)
        fragmentTransaction.commit()
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener {
                view, year, month, dayOfMonth ->
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val calendar = Calendar.getInstance()
            calendar[year, month] = dayOfMonth
            val sDate = sdf.format(calendar.time)
            dateString = sDate.toString()

            val fragment = CalendarFragment()
            val bundle = Bundle()
            bundle.putString("tvstartDate",dateString)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,fragment).commit()
            replaceFragment(CalendarFragment(),dateString)
        }
    }
}