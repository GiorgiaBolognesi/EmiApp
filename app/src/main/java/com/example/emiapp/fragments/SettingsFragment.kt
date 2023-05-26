package com.example.emiapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentTransaction
import com.example.emiapp.R
import com.example.emiapp.models.MyPreference


class SettingsFragment : Fragment() {
    lateinit var seekBar: SeekBar
    lateinit var intensity : TextView
    lateinit var switch: SwitchCompat
    lateinit var spinner: Spinner
    var text : String? = null
    var nightModeSwitched : Boolean = false
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myPreference: MyPreference
    val languageList:Array<String> = arrayOf("it", "en")
    lateinit var button: Button
    lateinit var audioManager: AudioManager
    lateinit var buttonPlus: Button
    lateinit var buttonMinus : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        seekBar = view.findViewById(R.id.seekbar)
        intensity = view.findViewById(R.id.intensity)
        spinner = view.findViewById(R.id.spinner)
        switch = view.findViewById(R.id.switch1)
        button = view.findViewById(R.id.button)
        buttonPlus = view.findViewById(R.id.up)
        buttonMinus = view.findViewById(R.id.down)

        //volume buttons
        buttonPlus.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            Toast.makeText(context,"Volume aumentato", Toast.LENGTH_SHORT).show();
        }

        buttonMinus.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            Toast.makeText(context,"Volume diminuito", Toast.LENGTH_SHORT).show();
        }

        myPreference = MyPreference(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

        //volume
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        //seekbar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                intensity.text = progress.toString()
                saveData()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        //spinner
        var languages = arrayOf("italiano", "inglese")
        var arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_text, languageList)
        spinner.adapter = arrayAdapter

        val lang: String? = myPreference.getLoginCount()
        val index = languageList.indexOf(lang)
        if(index >= 0) {
            spinner.setSelection(index)
        }
        button.setOnClickListener {
            myPreference.setLoginCount(languageList[spinner.selectedItemPosition])

        }

        switch.setOnClickListener {
            if(switch.isChecked) {
                //night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                nightModeSwitched = true
                if (nightModeSwitched == true) {
                    val profileFragment = SettingsFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.layout, profileFragment)
                    transaction.commit()
                }
                saveData()
            } else {
            //day mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            nightModeSwitched = false
            if (nightModeSwitched == false) {

                val profileFragment = SettingsFragment()
                val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.layout, profileFragment)
                transaction.commit()
             }
            }
            saveData()
        }
        loadData()

        return view
    }

    private fun loadData() {
        switch.isChecked = sharedPreferences.getBoolean("save_state", false)
        intensity.text = sharedPreferences.getString("save_string", null)
        seekBar.progress = sharedPreferences.getInt("save_progress", 0)
        text = sharedPreferences.getString("save_lan", null)
    }

    private fun saveData() {
        val editor = sharedPreferences.edit()
        editor.apply {
            putBoolean("save_state", switch.isChecked)
            putString("save_string", intensity.text.toString())
            putInt("save_progress", seekBar.progress)
            putString("save_lan", text)

        }.apply()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}