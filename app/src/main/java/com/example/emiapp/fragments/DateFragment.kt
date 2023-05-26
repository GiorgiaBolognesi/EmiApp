package com.example.emiapp.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.emiapp.R
import com.example.emiapp.activities.MainActivity
import com.example.emiapp.models.FragmentNavigation
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class DateFragment : Fragment() {
    private val myCalendar : Calendar = Calendar.getInstance()
    private lateinit var endDateText : TextView
    private lateinit var endTimeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_date, container, false)

        //DEFAULT VALUES
        val beginDate : TextView = view.findViewById(R.id.beginDate)
        val beginTime : TextView = view.findViewById(R.id.begindHour)
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        beginDate.setText(currentDate)
        beginTime.setText(currentTime)

        //ADD DATE PICKER
        val startDateButton: Button = view.findViewById<View>(R.id.beginDateButton) as Button
        val endDateButton: Button = view.findViewById<View>(R.id.endDateButton) as Button
        val startDateText: TextView = view.findViewById(R.id.beginDate) as TextView
        endDateText = view.findViewById(R.id.endDate) as TextView
        val nextButton: Button = view.findViewById(R.id.nextButton)
        val prevButton: Button = view.findViewById(R.id.prevButton)

        val dateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)

                val myFormat = "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                startDateText.setText(dateFormat.format(this.myCalendar.getTime()))

            }

        startDateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                context?.let {
                    DatePickerDialog(
                        it,
                        dateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                    ).apply { datePicker.maxDate = Date().time}
                        .show()
                }
            }
        })

        val secondDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)

                val myFormat = "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                endDateText.setText(dateFormat.format(this.myCalendar.getTime()))

            }
        endDateButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                context?.let {
                    DatePickerDialog(
                        it,
                        secondDateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                    ).apply { datePicker.maxDate = Date().time}
                        .show()
                }
            }
        })


        // ADD TIME PICKER
        val startTimeButton: Button = view.findViewById<View>(R.id.beginHourButton) as Button
        val endTimeButton: Button = view.findViewById<View>(R.id.endHourButton) as Button
        val startTimeText: TextView = view.findViewById(R.id.begindHour) as TextView
        endTimeText = view.findViewById(R.id.endHour) as TextView

        startTimeButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]

                TimePickerDialog(
                    context,
                    { timePicker, selectedHour, selectedMinute ->
                        startTimeText.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                    }, hour, minute, true
                ).show()
            }
        })

        endTimeButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]

                TimePickerDialog(
                    context,
                    { timePicker, selectedHour, selectedMinute ->
                        endTimeText.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                        //("$selectedHour:$selectedMinute")
                    }, hour, minute, true
                ).show()
            }
        })

        nextButton.setOnClickListener {
            val sDText = startDateText.text.toString()
            val sTText = startTimeText.text.toString()
            val eDText = endDateText.text.toString()
            val eTText = endTimeText.text.toString()

            if (TextUtils.isEmpty(eDText.trim())) {
                Toast.makeText(context, getString(R.string.insertEndDate), Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(eTText.trim())) {
                Toast.makeText(context, getString(R.string.insertEndTime), Toast.LENGTH_SHORT).show()
            }
            else {
                //DURATION
                val simpleDateFormat = SimpleDateFormat("HH:mm")
                val startDate = simpleDateFormat.parse(startTimeText.text.toString())
                val endDate = simpleDateFormat.parse(endTimeText.text.toString())

                val simpleDayFormat = SimpleDateFormat("dd/MM/yyyy")
                val startDay = simpleDayFormat.parse(startDateText.text.toString())
                val endDay = simpleDayFormat.parse(endDateText.text.toString())

                var difference: Long = endDate.getTime() - startDate.getTime()
                val diffdays: Long = endDay.getTime() - startDay.getTime()
                var durDays = (diffdays / (1000 * 60 * 60 * 24)).toInt()

                val time = Calendar.getInstance().time
                val currentDate = simpleDayFormat.format(time)
                val currentTime = simpleDateFormat.format(time)
                val currentDateParsed = simpleDayFormat.parse(currentDate)
                val currentTimeParsed = simpleDateFormat.parse(currentTime)



                if ((durDays == 0 && difference < 0)) {
                    Toast.makeText(context, getString(R.string.alertDays), Toast.LENGTH_SHORT)
                        .show()
                }
                else {

                    if (durDays > 0 && difference < 0) {
                        durDays = durDays-1
                        val dateMax = simpleDateFormat.parse("24:00")
                        val dateMin = simpleDateFormat.parse("00:00")
                        difference = (dateMax.time - startDate.time) + (endDate.time - dateMin.time)
                    }

                    val days = (difference / (1000 * 60 * 60 * 24)).toInt()
                    val hours =
                        ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60)).toInt()
                    val min =
                        ((difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60)).toInt()

                    if (durDays == 0 && hours == 0 && min == 0) {
                        Toast.makeText(
                            context,
                            getString(R.string.alertDuration),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (durDays < 0) {
                        Toast.makeText(context, getString(R.string.alertDays), Toast.LENGTH_SHORT)
                            .show()
                    } else if ((currentDateParsed.time - endDay.time <= 0) && (currentTimeParsed.time-endDate.time <0)){
                        Toast.makeText(context, getString(R.string.notValid), Toast.LENGTH_SHORT)
                            .show()
                    }
                    else {
                        val result = Bundle()
                        result.putString("startDate", sDText)
                        result.putString("startTime", sTText)
                        result.putString("endDate", eDText)
                        result.putString("endTime", eTText)
                        result.putString("day", durDays.toString())
                        result.putString("hour", hours.toString())
                        result.putString("min", min.toString())

                        parentFragmentManager.setFragmentResult("datafromDate", result)
                        var nav = activity as FragmentNavigation
                        nav.navigateFrag(TypeFragment(), false)
                    }
                }
            }
        }

        prevButton.setOnClickListener {
            val intent = Intent(this.context, MainActivity::class.java)
            context?.startActivity(intent)
        }

        //PROGRESS BAR
        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)
        progressBar.max = 1000
        val currentProgress = 200
        ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
            .setDuration(2000)
            .start()

        return view
    }
}