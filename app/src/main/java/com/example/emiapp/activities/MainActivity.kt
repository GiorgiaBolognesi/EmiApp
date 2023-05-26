package com.example.emiapp.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.emiapp.R
import com.example.emiapp.fragments.PasswordFragment
import com.example.emiapp.fragments.SettingsFragment
import com.example.emiapp.fragments.ShowProfileFragment
import com.example.emiapp.models.MyContextWrapper
import com.example.emiapp.models.MyPreference
import com.example.emiapp.models.runInstantWorker
import com.example.emiapp.models.startPeriodicWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var header_layout : NavigationView
    lateinit var email: TextView
    lateinit var calendarButton: FloatingActionButton
    lateinit var addHeadacheButton : FloatingActionButton
    private lateinit var textDuration : TextView
    lateinit var texDuration : TextView
    lateinit var daysWithout : TextView
    private  var endTime: String? = null
    private var endDate: String? = null
    private  var endTimeFromResume: String? = null
    private var endDateFromResume: String? = null
    private var days: Int? = null
    private var hours: Int? = null
    private var min: Int? = null
    private var durDays: Int?=null
    var durationD: String = ""
    var durationH: String = ""
    var durationM: String = ""
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val PERMISSION_NEEDED = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    private var PERMISSION_POST_NOTIFICATION : Boolean = false
    private var allPermissionGranted : Boolean? = null
    lateinit var myPreference: MyPreference

    /////////////////////////
    private lateinit var currentUser : FirebaseUser
    private lateinit var mAuth : FirebaseAuth
    private lateinit var storage: StorageReference
    //////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent : Intent = getIntent()
        addHeadacheButton = findViewById(R.id.addHeadhacheButton)
        calendarButton = findViewById(R.id.CalendarButton)
        endDateFromResume = intent.getStringExtra("endDate").toString()
        endTimeFromResume = intent.getStringExtra("endTime").toString()
        textDuration = findViewById(R.id.textDuration)

        ////////////////////////////////
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!
        updateNavHeader()
        /////////////////////////////////

        //menu
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle =  ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId) {

                R.id.nav_pass ->  replaceFragment(PasswordFragment(), it.title.toString())
                R.id.nav_profile -> replaceFragment(ShowProfileFragment(), it.title.toString())
                R.id.nav_settings ->  replaceFragment(SettingsFragment(), it.title.toString())

                (R.id.nav_home) -> {
                    val intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)
                }


                (R.id.nav_logout) -> {

                    //dialog
                    val builder = android.app.AlertDialog.Builder(this)
                    val view = layoutInflater.inflate(R.layout.dialog_logout, null)


                    builder.setView(view)
                    val dialog = builder.create()



                    view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
                        //logout
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        this.startActivity(intent)
                        Toast.makeText(this, "Logout effettuato con sucesso!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                    view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                        dialog.dismiss()
                    }

                    if (dialog.window != null) {
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                    }

                    dialog.show() }
            }

            true
        }

        //LOADDATA
        val appContext = applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPrefsMain", Context.MODE_PRIVATE)
        endDate = sharedPreferences.getString("DateList","")
        endTime = sharedPreferences.getString("TimeList","")
        daysWithout = findViewById(R.id.daysWithout) as TextView

        val handler =  Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                durationBetween()

                if (durDays != null && durDays != 0) {
                    if (durDays == 1) {
                        durationD = durDays.toString() + " giorno "
                    }
                    else {
                        durationD = durDays.toString() + " giorni "
                    }
                }
                if (hours != null && hours != 0) {
                    if (hours == 1) {
                        durationH =  hours.toString() + " ora "
                    }
                    else {
                        durationH = hours.toString() + " ore "
                    }
                }
                if (min != null && min != 0) {
                    if (min == 1) {
                        durationM =  min.toString() + " minuto "
                    }
                    else {
                        durationM = min.toString() + " minuti "
                    }
                }
                else if ((durDays == null ) && (hours == null) && (min == null)){
                    textDuration.setText("Non hai ancora avuto un attacco!")
                }
                else if (durDays == 0 &&  hours == 0 && min == 0) {
                    durationM =   "1 minuto "
                }
                daysWithout.setText(durationD + durationH + durationM)
                handler.postDelayed(this, 1000)
            }
        }, 1000)

        startPeriodicWorker(this)
        runInstantWorker(this)
        requestPermissionNotification()

        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            this.startActivity(intent)
        }

        addHeadacheButton.setOnClickListener {
            val intent = Intent(this, AddHeadacheActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun updateNavHeader() {
        val navView : NavigationView = findViewById(R.id.nav_view)
        val headerView : View = navView.getHeaderView(0)
        val navEmail = headerView.findViewById<TextView>(R.id.tvemailnav)
        val navImage = headerView.findViewById<ImageView>(R.id.tvimagenav)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        navEmail.setText(currentUser.email)

        storage = FirebaseStorage.getInstance().getReference("Task Images/"+userId)

        val localfile = File.createTempFile("tempImage", "jpg")
        storage.getFile(localfile).addOnCompleteListener {

            if (it.isSuccessful) {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                navImage.setImageBitmap(bitmap)
            }
            else {

                navImage.setImageDrawable(getResources().getDrawable(R.drawable.profile_avatar));
            }



        }.addOnFailureListener {
            //Toast.makeText(context, "Errore", Toast.LENGTH_SHORT).show()
        }

    }

    private fun replaceFragment(fragment: Fragment, title : String) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FrameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

        addHeadacheButton.visibility = View.GONE
        calendarButton.visibility = View.GONE
        textDuration.visibility = View.GONE
        //textNew.visibility = View.GONE
        daysWithout.visibility = View.GONE
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)) {

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allPermissionGranted = false
                return
            }
        }
        this.allPermissionGranted = true
        finish()
    }

    private fun requestPermissionNotification() {
        if (ContextCompat.checkSelfPermission(this,PERMISSION_NEEDED[0]) == PackageManager.PERMISSION_GRANTED) {
            PERMISSION_POST_NOTIFICATION = true

        }
        else {
            val requestNotificationDialog: String = getString(R.string.notificationDialog)
            showPermissionDialog(requestNotificationDialog)
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                PERMISSION_POST_NOTIFICATION = true
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
            } else {
                PERMISSION_POST_NOTIFICATION=false
            }
        }

    private fun showPermissionDialog(string: String) {
        AlertDialog.Builder(
            this)
            .setTitle(getString(R.string.requestPermission))
            .setPositiveButton("OK") {
                    dialog: DialogInterface, which: Int ->
                requestPermissionLauncher.launch(PERMISSION_NEEDED[0])
                dialog.dismiss()
            }
            .setNegativeButton("NO") {
                    dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }
            .setMessage(string)
            .show()
    }

    private fun durationBetween() {
        if(endDate != "" && endTime!= "") {
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val startTimeMain = simpleDateFormat.parse(endTime.toString())
            val endTimeMain = simpleDateFormat.parse(currentTime.toString())
            val simpleDayFormat = SimpleDateFormat("dd/MM/yyyy")
            val startDateMain = simpleDayFormat.parse(endDate.toString())
            val endDateMain = simpleDayFormat.parse(currentDate.toString())
            var difference: Long = endTimeMain.getTime() - startTimeMain.getTime()
            val diffdays: Long = endDateMain.getTime() - startDateMain.getTime()
            durDays = (diffdays / (1000 * 60 * 60 * 24)).toInt()

            if (durDays!! > 0 && difference < 0) {
                durDays = durDays!! - 1
                val dateMax = simpleDateFormat.parse("24:00")
                val dateMin = simpleDateFormat.parse("00:00")
                difference = (dateMax.time - startTimeMain.time) + (endTimeMain.time - dateMin.time)
            }

            days = (difference / (1000 * 60 * 60 * 24)).toInt()
            hours =
                ((difference - (1000 * 60 * 60 * 24 * days!!)) / (1000 * 60 * 60)).toInt()
            min =
                ((difference - (1000 * 60 * 60 * 24 * days!!) - (1000 * 60 * 60 * hours!!)) / (1000 * 60)).toInt()

            if(min!! <0) {
                min = 0}

            if(hours!! <0) {
                hours = 0
            }

            if (durDays == 3) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            saveData()
        }
    }
    private fun saveData() {
        val appContext = applicationContext
        val sharedPreferences = appContext.getSharedPreferences("sharedPrefsMain", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("durDays",durDays.toString())
        editor.apply()
    }

    override fun attachBaseContext(newBase: Context?) {
        myPreference = MyPreference(newBase!!)
        val lang = myPreference.getLoginCount()
        super.attachBaseContext(MyContextWrapper.wrap(newBase, lang!!))
    }
}