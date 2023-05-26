package com.example.emiapp.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.emiapp.R
import com.example.emiapp.models.FragmentNavigation
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SymptomsFragment : Fragment() {

    private var dialog: AlertDialog?= null
    lateinit var layout: FlexboxLayout
    private var counter: Int = 1000
    var symptoms: String = ""
    lateinit var n : Button
    lateinit var checkSymptoms: CheckBox
    private val mViews: ArrayList<View> = ArrayList()
    private val checkSy: ArrayList<CheckBox> = ArrayList()
    private var arraySymptoms = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_symptoms, container, false)
        val nextButton: Button = view.findViewById(R.id.nextButton)
        val prevButton: Button = view.findViewById(R.id.prevButton)
        val addButton: Button = view.findViewById(R.id.plusButton)
        layout=view.findViewById(R.id.containerSympFlex)
        buildDialog()
        n = view.findViewById(R.id.nextButton)

        loadData()

        addButton.setOnClickListener {
            dialog?.show()
        }

        nextButton.setOnClickListener {

            checkIfValid()
            val result = Bundle()
            result.putString("Symptoms", symptoms)
            parentFragmentManager.setFragmentResult("datafromSymp", result)
            var navType = activity as FragmentNavigation
            navType.navigateFrag(PainFragment(),false)
        }

        prevButton.setOnClickListener {
            var navType = activity as FragmentNavigation
            navType.navigateFrag(TypeFragment(),false)
        }

        //PROGRESS BAR
        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)
        progressBar.max = 1000
        val currentProgress = 600
        ObjectAnimator.ofInt(progressBar, "progress",400, currentProgress)
            .setDuration(2000)
            .start()

        return view
    }

    private fun checkIfValid() {
        for(i in 0..layout.childCount) {
            val checkB: CheckBox? = layout.getChildAt(i) as? CheckBox
            if (checkB != null) {
                checkSy.add(checkB)
                if (checkB.isChecked) {
                    symptoms = symptoms + "\n"+ checkB.text.toString()
                }
            }
        }
    }

    private fun loadData() {
        val appContext = requireContext().applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("taskListSymptoms", "[]")
        val type = object: TypeToken<ArrayList<String>>() {
        }.type
        if(json == null)
            arraySymptoms = ArrayList()
        else
            arraySymptoms = gson.fromJson(json, type)

        for(i in 0..arraySymptoms.size-1){
            for(j in i+1..arraySymptoms.size-1) {
                if (arraySymptoms[i] == arraySymptoms[j]) {
                    break
                }
                else if (j == arraySymptoms.size-1) {
                    addCheck(arraySymptoms[i])
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun addCheck(symptomText: String) {
        counter += 1
        checkSymptoms = CheckBox(context)
        checkSymptoms.text = symptomText
        checkSymptoms.textSize = 20F
        checkSymptoms.setTextColor(Color.rgb(2, 54,104))
        checkSymptoms.gravity = Gravity.CENTER
        checkSymptoms.setPadding(15, 15, 15, 15)
        checkSymptoms.setLeftTopRightBottom(10, 10,10,10)
        val top = resources.getDrawable(R.drawable.migranecheck)
        checkSymptoms.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null)
        top.setTint(Color.rgb(29, 98,166))
        checkSymptoms.buttonDrawable = null
        checkSymptoms.id = counter
        layout.addView(checkSymptoms)
        mViews.add(View(context))
        arraySymptoms.add(symptomText)
        saveData()
    }

    private fun saveData() {
        val appContext = requireContext().applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(arraySymptoms)
        val editor = sharedPreferences.edit()
        editor.putString("taskListSymptoms", json)
        editor.apply()
    }

    private fun buildDialog() {
        val builder = AlertDialog.Builder(context)
        val v: View = layoutInflater.inflate(R.layout.dialogsymptoms,null)
        val symptom: EditText = v.findViewById(R.id.textdialogsymptoms)
        builder.setView(v)
        builder.setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
            addCheck(symptom.getText().toString())
        }
            .setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
            }
        dialog = builder.create()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SymptomsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}