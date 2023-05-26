package com.example.emiapp.fragments

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.emiapp.R
import com.example.emiapp.models.FragmentNavigation


class PainFragment : Fragment() {
    var painH: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pain, container, false)
        val nextButton: Button = view.findViewById(R.id.nextButton)
        val prevButton: Button = view.findViewById(R.id.prevButton)
        val slider: SeekBar = view.findViewById(R.id.intensitySeekBar) as SeekBar
        var value: TextView = view.findViewById(R.id.intensity) as TextView
        slider.max = 100
        value .text= getString(R.string.noPain)

        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val image: ImageView = view.findViewById(R.id.headache) as ImageView

                if (progress >= 0) {
                    value.text = getString(R.string.noPain)
                    image.alpha = 0.2F
                }
                if (progress >= 25 ) {
                    value.text = getString(R.string.mild)
                    image.alpha = 0.2F
                }
                if (progress >= 50 ) {
                    value.text = getString(R.string.moderate)
                    image.alpha = 0.4F
                }
                if (progress >= 75 ) {
                    value.text = getString(R.string.severe)
                    image.alpha = 0.6F
                }
                if (progress >= 100 ) {
                    value.text = getString(R.string.intense)
                    image.alpha = 1F
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        nextButton.setOnClickListener {

            if (value.text == getString(R.string.noPain)) {
                painH = getString(R.string.noPain)
            }
            if (value.text == getString(R.string.mild)) {
                painH = getString(R.string.mild)
            }
            if (value.text == getString(R.string.moderate)) {
                painH = getString(R.string.moderate)
            }
            if (value.text == getString(R.string.severe)) {
                painH = getString(R.string.severe)
            }
            if (value.text == getString(R.string.intense)) {
                painH = getString(R.string.intense)
            }

            val result = Bundle()
            result.putString("Pain", painH )
            parentFragmentManager.setFragmentResult("datafromPain", result)
            var navPain = activity as FragmentNavigation
            navPain.navigateFrag(NoteFragment(), false)
        }

        prevButton.setOnClickListener {
            var navPain = activity as FragmentNavigation
            navPain.navigateFrag(SymptomsFragment(), false)
        }


        //PROGRESS BAR
        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)
        progressBar.max = 1000
        val currentProgress = 800
        ObjectAnimator.ofInt(progressBar, "progress",600,  currentProgress)
            .setDuration(2000)
            .start()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}