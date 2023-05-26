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
        val value: TextView = view.findViewById(R.id.intensity) as TextView



        slider.max = 100 //Default is 100

        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val image: ImageView = view.findViewById(R.id.headache) as ImageView


                if (progress == 0 ) {
                    value.text = "Nessun dolore"
                    image.alpha = 0.2F
                }

                if (progress == 25 ) {
                    value.text = "Lieve"
                    image.alpha = 0.2F
                    value.setTextColor(Color.GREEN)
                }

                if (progress == 50 ) {
                    value.text = "Moderato"
                    image.alpha = 0.4F
                    value.setTextColor(Color.YELLOW)
                }

                if (progress == 75 ) {
                    value.text = "Severo"
                    image.alpha = 0.6F
                    value.setTextColor(Color.MAGENTA)
                }

                if (progress == 100 ) {
                    value.text = "Dolore intenso"
                    image.alpha = 1F
                    value.setTextColor(Color.RED)
                }

                /* value.text = progress.toString() */

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })




        nextButton.setOnClickListener {


            if (value.text == "Nessun dolore") {
                painH = "Nessun dolore"
            }
            if (value.text == "Lieve") {
                painH = "Lieve"
            }
            if (value.text == "Moderato") {
                painH = "Moderato"
            }
            if (value.text == "Severo") {
                painH = "Severo"
            }
            if (value.text == "Dolore intenso") {
                painH = "Dolore intenso"
            }

            Toast.makeText(requireContext(),painH.toString(), Toast.LENGTH_SHORT).show()



            val result = Bundle()
            result.putString("Pain", painH )

            parentFragmentManager.setFragmentResult("datafrom4", result)


            var navPain = activity as FragmentNavigation
            navPain.navigateFrag(NoteFragment(), false)
        }


        prevButton.setOnClickListener {

            var navPain = activity as FragmentNavigation
            navPain.navigateFrag(TypeFragment(), false)
        }


        //Progress Var
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