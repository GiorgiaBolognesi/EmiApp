package com.example.emiapp.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.emiapp.R
import com.example.emiapp.models.FragmentNavigation
import java.text.SimpleDateFormat
import java.util.*

class NoteFragment : Fragment() {
    var noteH: String = ""

    lateinit var sugg1 : Button
    lateinit var sugg2 : Button
    lateinit var sugg3 : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        val nextButton: Button = view.findViewById(R.id.nextButton)
        val prevButton: Button = view.findViewById(R.id.prevButton)

        var note : EditText = view.findViewById(R.id.Note) as EditText

        var sug1 = view.findViewById<Button>(R.id.suggestion1)
        var sug2 = view.findViewById<Button>(R.id.suggestion2)
        var sug3 = view.findViewById<Button>(R.id.suggestion3)
        var sugDate = view.findViewById<Button>(R.id.suggestionDate)
        var sugTime = view.findViewById<Button>(R.id.suggestionTime)

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        sugDate.setOnClickListener {


            val nota = note.text

            val sb = StringBuilder()
            sb.append(nota).append(" ").append(getString(R.string.curentDate)).append(" ").append(currentDate).append("\n")

            note.setText(sb)


        }

        sugTime.setOnClickListener {

            val nota = note.text

            val sb = StringBuilder()
            sb.append(nota).append(" ").append(getString(R.string.currentTime)).append(" ").append(currentTime).append("\n")

            note.setText(sb)
        }


        sug1.setOnClickListener {

            val nota = note.text

            val sb = StringBuilder()
            sb.append(nota).append(" ").append("il livello di dolore è diminuito\n")

            note.setText(sb)
        }

        sug2.setOnClickListener {


            val nota = note.text

            val sb = StringBuilder()
            sb.append(nota).append(" ").append("l'attacco sembra essere terminato\n")

            note.setText(sb)
        }

        sug3.setOnClickListener {

            val nota = note.text

            val sb = StringBuilder()
            sb.append(nota).append(" ").append("questo sintomo è diminuito di intensità:\n")

            note.setText(sb)
        }











        nextButton.setOnClickListener {

            if (note.text != null) {
                noteH = note.text.toString()
            }

            Toast.makeText(requireContext(),noteH.toString(), Toast.LENGTH_SHORT).show()



            val result = Bundle()
            result.putString("Note", noteH )
            parentFragmentManager.setFragmentResult("datafrom5", result)


            var navNote = activity as FragmentNavigation
            navNote.navigateFrag(ResumeFragment(), false)
        }


        prevButton.setOnClickListener {

            var navNote = activity as FragmentNavigation
            navNote.navigateFrag(PainFragment(), false)
        }

        //Progress Var
        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)

        progressBar.max = 1000

        val currentProgress = 1000

        ObjectAnimator.ofInt(progressBar, "progress",800, currentProgress)
            .setDuration(2000)
            .start()



        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}