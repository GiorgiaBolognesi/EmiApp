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

class TypeFragment : Fragment() {
    private var dialog: AlertDialog?= null
    lateinit var layout: FlexboxLayout
    private var counter: Int = 1
    var typeH: String = ""
    lateinit var n : Button
    lateinit var checkB: CheckBox

    private val mViews: ArrayList<View> = ArrayList()
    private val checkBB: ArrayList<CheckBox> = ArrayList()
    private var arraycheck = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //   initializeViews()

        }
    }

    /*  private fun initializeViews() {
           mViews.add( View(context))
          mViews.add( View(context))
          mViews.add( View(context))
          mViews.add( View(context))
          mViews.add( View(context))
      }*/

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_type, container, false)
        val nextButton: Button = view.findViewById(R.id.nextButton)
        val migraineCheck: CheckBox = view.findViewById(R.id.emicrania) as CheckBox
        val headacheCheck: CheckBox = view.findViewById(R.id.cefalea) as CheckBox
        val bunchCheck: CheckBox = view.findViewById(R.id.grappolo) as CheckBox
        val prevButton: Button = view.findViewById(R.id.prevButton)
        val addButton: Button = view.findViewById(R.id.plusButton)
        layout=view.findViewById(R.id.containerTypeFlex)
        buildDialog()
        // initializeViews()
        n = view.findViewById(R.id.nextButton)

        loadData()
        // arraycheck.add(migraineCheck.text.toString())

/*
        for(i in 0 until checkBB.size) {
            val checkB: CheckBox? = checkBB.get(i)
            if (checkB != null) {
                addCheck(checkB.text.toString())
            }
        }*/


        addButton.setOnClickListener {
            dialog?.show()
        }

        nextButton.setOnClickListener {

            if(migraineCheck.isChecked) {
                typeH = typeH + " " + "emicrania\n"
            }

            if(headacheCheck.isChecked) {
                typeH = typeH + " "+ "cefalea\n"
            }

            if (bunchCheck.isChecked) {
                typeH = typeH + " "+ "emicrania a grappolo\n"
            }

            checkIfValid()

            // Toast.makeText(requireContext(), typeH,Toast.LENGTH_SHORT).show()

            val result = Bundle()
            result.putString("Typeh", typeH)
            parentFragmentManager.setFragmentResult("datafrom2", result)

            var navType = activity as FragmentNavigation
            navType.navigateFrag(SymptomsFragment(),false)
        }

        prevButton.setOnClickListener {
            var navType = activity as FragmentNavigation
            navType.navigateFrag(DateFragment(),false)
        }


        //Progress Var
        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)

        progressBar.max = 1000

        val currentProgress = 400

        ObjectAnimator.ofInt(progressBar, "progress",200,  currentProgress)
            .setDuration(2000)
            .start()






        return view
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun loadData() {
        val appContext = requireContext().applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPreferences.getString("taskList", "[]")
        val type = object: TypeToken<ArrayList<String>>() {
        }.type

        if(json == null)
            arraycheck = ArrayList()
        else
            arraycheck = gson.fromJson(json, type)

        for(i in 0..arraycheck.size-1){
            for(j in i+1..arraycheck.size-1) {
                if (arraycheck[i] == arraycheck[j]) {
                    break
                }
                else if (j == arraycheck.size-1) {
                    addCheck(arraycheck[i])
                }
            }
        }
    }

    private fun checkIfValid() {
        for(i in 0..layout.childCount) {
            val checkB: CheckBox? = layout.getChildAt(i) as? CheckBox
            if (checkB != null) {
                checkBB.add(checkB)
                if (checkB.isChecked) {
                    typeH = typeH + " \n"+ checkB.text.toString()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun buildDialog() {

        val builder = AlertDialog.Builder(context)
        val v: View = layoutInflater.inflate(R.layout.dialogtype,null)
        val type: EditText = v.findViewById(R.id.textdialog)
        builder.setView(v)
        builder.setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
            addCheck(type.getText().toString())
        }
            .setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
            }
        dialog = builder.create()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ResourceAsColor")
    private fun addCheck(typeText: String) {

        //arraycheck.add(typeText)

        counter += 1
        checkB = CheckBox(context)

        checkB.text = typeText
        checkB.textSize = 20F
        checkB.setTextColor(Color.rgb(2, 54,104))
        checkB.setGravity(Gravity.CENTER)

        checkB.setPadding(12,12,12,12)
        //checkB.setButtonDrawable(resources.getDrawable(R.drawable.migranecheck))

        val top = resources.getDrawable(R.drawable.migranecheck)
        checkB.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null)
        top.setTint(Color.rgb(29, 98,166))

        checkB.buttonDrawable = null
        checkB.id = counter

        layout.addView(checkB)
        mViews.add(View(context))

        arraycheck.add(typeText)
        saveData()

    }

    private fun saveData() {
        val checkText : String = checkB.text.toString()

        val appContext = requireContext().applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(arraycheck)
        val editor = sharedPreferences.edit()
        editor.putString("taskList", json)
        editor.apply()
        /* editor.apply {
             putString("STRING_KEY",  checkText)
         }.apply()*/
        Toast.makeText(context,"Data saved", Toast.LENGTH_SHORT).show()



    }
}