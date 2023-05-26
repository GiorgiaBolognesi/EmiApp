package com.example.emiapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.emiapp.R
import com.example.emiapp.activities.MainActivity
import com.example.emiapp.models.Data
import com.example.emiapp.models.ViewModel
import java.text.SimpleDateFormat
import java.util.*


class ResumeFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    lateinit var startDate: TextView
    lateinit var endDate: TextView
    lateinit var endTime: TextView
    lateinit var durH: TextView
    lateinit var durM: TextView
    lateinit var durD: TextView
    lateinit var typeHead: TextView
    lateinit var sympHead: TextView
    lateinit var startTime: TextView
    lateinit var painHead: TextView
    lateinit var noteHead: TextView
    lateinit var imagePain : ImageView
    lateinit var oldParsed: Date
    lateinit var newParsed: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {//bundle->
            //datas =bundle.getParcelable("datas")!!
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resume, container, false)
        startDate = view.findViewById(R.id.dateB)
        startTime = view.findViewById(R.id.timeB)
        endDate = view.findViewById(R.id.dateF)
        endTime = view.findViewById(R.id.timeF)
        durH = view.findViewById(R.id.durH)
        durM = view.findViewById(R.id.durM)
        durD = view.findViewById(R.id.durD)
        typeHead = view.findViewById(R.id.typeH)
        sympHead = view.findViewById(R.id.symptoms)
        painHead = view.findViewById(R.id.painH)
        noteHead = view.findViewById(R.id.noteH)
        val saveButton : Button = view.findViewById(R.id.saveButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
        imagePain = view.findViewById(R.id.painImg)

        parentFragmentManager.setFragmentResultListener("datafromDate",this, FragmentResultListener(
            fun(requestedKey:String, result:Bundle) {
                startDate.setText(result.getString("startDate"))
                startTime.setText(result.getString("startTime"))
                endDate.setText(result.getString("endDate"))
                endTime.setText(result.getString("endTime"))
                if (result.getString("day") != "0") {
                    durD.setText(result.getString("day") + "d")
                }
                if (result.getString("hour") != "0") {
                    durH.setText(result.getString("hour") + "h")
                }
                if(result.getString("min") != "0") {
                    durM.setText(result.getString("min") + "m")
                }

            }
        ))
        parentFragmentManager.setFragmentResultListener("datafromType",this, FragmentResultListener(
            fun(requestedKey:String, result:Bundle) {
                typeHead.setText(result.getString("Typeh"))

                if (result.getString("Typeh")!!.isEmpty()) {
                    typeHead.setText(getString(R.string.noType))
                    typeHead.gravity= Gravity.CENTER
                    typeHead.textSize= 20F
                }
            }
        ))

        parentFragmentManager.setFragmentResultListener("datafromSymp",this, FragmentResultListener(
            fun(requestedKey:String, result:Bundle) {
                sympHead.setText(result.getString("Symptoms"))

                if (result.getString("Symptoms")!!.isEmpty()) {
                    sympHead.setText(getString(R.string.noSymp))
                    sympHead.gravity= Gravity.CENTER
                    sympHead.textSize= 20F
                }
            }
        ))

        parentFragmentManager.setFragmentResultListener("datafromPain",this, FragmentResultListener(
            fun(requestedKey:String, result:Bundle) {
                painHead.setText(result.getString("Pain"))

                if(result.getString("Pain") == getString(R.string.noPain)) {

                    imagePain.setImageDrawable(getResources().getDrawable(R.drawable.happy))
                    imagePain.setColorFilter(Color.rgb(0, 204,0))

                } else if (result.getString("Pain") == getString(R.string.mild)) {
                    imagePain.setImageDrawable(getResources().getDrawable(R.drawable.lieve))
                    imagePain.setColorFilter(Color.rgb(0, 102,0))

                } else if (result.getString("Pain") == getString(R.string.moderate)) {
                    imagePain.setImageDrawable(getResources().getDrawable(R.drawable.sad))
                    imagePain.setColorFilter(Color.rgb(255, 128,0))

                } else if (result.getString("Pain") == getString(R.string.severe)) {
                    imagePain.setImageDrawable(getResources().getDrawable(R.drawable.severe))
                    imagePain.setColorFilter(Color.rgb(255, 0,127))

                } else if (result.getString("Pain") == getString(R.string.intense)) {
                    imagePain.setImageDrawable(getResources().getDrawable(R.drawable.worst))
                    imagePain.setColorFilter(Color.rgb(255, 0,0))
                }
            }
        ))

        parentFragmentManager.setFragmentResultListener("datafromNote",this, FragmentResultListener(
            fun(requestedKey:String, result:Bundle) {
                noteHead.setText(result.getString("Note"))

                if (result.getString("Note")!!.isEmpty()) {
                    noteHead.setText(getString(R.string.noComment))
                    noteHead.gravity= Gravity.CENTER
                }
            }
        ))

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if(it == null) {
                getString(R.string.addeddata)
            }
            else {
                getString(R.string.errorData,it.message)
            }
            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()

        } )

        saveButton.setOnClickListener {
            val data = Data()
            data.startDate = startDate.text.toString()
            data.startTime = startTime.text.toString()
            data.endDate = endDate.text.toString()
            data.endTime = endTime.text.toString()
            data.typeHeadHache = typeHead.text.toString()
            data.symptomsHeadHache = sympHead.text.toString()
            data.painHeadache = painHead.text.toString()
            data.note = noteHead.text.toString()

            viewModel.addData(data)
            val intent : Intent = Intent(this.context, MainActivity::class.java)
            context?.startActivity(intent)

            saveData()

        }

        //Dialog
        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val view = layoutInflater.inflate(R.layout.dialogresume, null)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
                deleteResume()
                Toast.makeText(context,getString(R.string.cancelled), Toast.LENGTH_SHORT).show()
            }

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            dialog.show()

        }

        return view
    }

    private fun saveData() {
        val appContext = requireContext().applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPrefsMain", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val simpleDayFormat = SimpleDateFormat("dd/MM/yyyy")
        val F = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        val time = Calendar.getInstance().time
        val currentDate = simpleDayFormat.parse(endDate.text.toString())
        val currentTime = simpleDateFormat.parse(endTime.text.toString())
        oldParsed = F.parse((endDate.text.toString()) + " "+ endTime.text.toString())
        val tvendDate = sharedPreferences.getString("DateList","")
        val tvendTime = sharedPreferences.getString("TimeList","")

        if(tvendTime != "" && tvendDate != "") {
            newParsed = F.parse(tvendDate + " " + tvendTime)

            if (oldParsed.time - newParsed.time >= 0) {
                editor.putString("DateList", endDate.text.toString())
                editor.putString("TimeList", endTime.text.toString())
                editor.apply()

            } else {

            }
        } else {
            editor.putString("DateList", endDate.text.toString())
            editor.putString("TimeList", endTime.text.toString())
            editor.apply()
        }
    }

    private fun deleteResume() {
        startDate.setText("")
        startTime.setText("")
        endDate.setText("")
        endTime.setText("")
        durH.setText("")
        durM.setText("")
        durD.setText("")
        typeHead.setText("")
        sympHead.setText("")
        painHead.setText("")
        noteHead.setText("")

        val intent : Intent = Intent(this.context, MainActivity::class.java)
        context?.startActivity(intent)
    }
}