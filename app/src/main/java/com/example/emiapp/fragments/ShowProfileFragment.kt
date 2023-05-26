package com.example.emiapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.emiapp.R
import com.example.emiapp.models.ProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ShowProfileFragment : Fragment() {

    private lateinit var tvName : EditText
    private lateinit var tvSex : EditText
    private lateinit var tvDate : TextView
    private lateinit var button : Button
    lateinit var birthday : Button
    private lateinit var image : CircleImageView
    lateinit var email : TextView
    lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    var imageURL :String? = null
    var uri: Uri? = null
    val user:String? = Firebase.auth.currentUser?.uid
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    var clickled : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_show_profile, container, false)
        database = Firebase.database.reference
        tvName = view.findViewById(R.id.profileName)
        tvSex = view.findViewById(R.id.profileSex)
        tvDate = view.findViewById(R.id.profileDate)
        button = view.findViewById(R.id.saveButton)
        birthday = view.findViewById(R.id.dataNascita)
        email = view.findViewById(R.id.emailProfile)
        image = view.findViewById(R.id.profileImg)

        var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //show user email
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        val userEmail = currentUser.email
        email.text = userEmail

        //choose date
        birthday.setOnClickListener(View.OnClickListener {
            val getDate : Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectDate : Calendar = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date : String= formatDate.format(selectDate.time)
                Toast.makeText(context, "Data: " +date, Toast.LENGTH_SHORT).show()
                tvDate.setText(date)

            }, getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

        })

        //save image
        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data!!.data
                image.setImageURI(uri)
            } else {
                Toast.makeText(context, getString(R.string.noImage), Toast.LENGTH_SHORT).show()
            }
        }
        image.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
            clickled = true
        }

        //image
        storage = FirebaseStorage.getInstance().getReference("Task Images/"+userId)
        val localfile = File.createTempFile("tempImage", "jpg")
        storage.getFile(localfile).addOnCompleteListener {
            if (it.isSuccessful) {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                image.setImageBitmap(bitmap)
            }
            else {
                image.setImageDrawable(getResources().getDrawable(R.drawable.profile_avatar));
            }
        }.addOnFailureListener {
        }

        //show user data
        database.child("UserInfo").child(userId).get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("dataName").value.toString()
                val sex = it.child("dataSesso").value.toString()
                val date = it.child("dataNascita").value.toString()
                tvName.setText(name)
                tvSex.setText(sex)
                tvDate.text = date

            } else {
                tvName.setText("opzionale")
                tvSex.setText("opzionale")
                tvDate.setText("opzionale")

            }

        }.addOnFailureListener {
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            savedata()
        }

        return view
    }

    //save image into firebase
    private fun savedata() {
        val storageReference =
            FirebaseStorage.getInstance().getReference("Task Images/" + auth.currentUser?.uid)
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()
        if (clickled == false) {
            storage = FirebaseStorage.getInstance().getReference("Task Images/"+userId)
            val localfile = File.createTempFile("tempImage", "jpg")
            storage.getFile(localfile).addOnCompleteListener {

                if (it.isSuccessful) {
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    image.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(context, "problem", Toast.LENGTH_SHORT).show()
                }
            }

            uploadData()
            dialog.dismiss()

        } else {

            storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                var uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageURL = urlImage.toString()
                uploadData()
                dialog.dismiss()
            }.addOnFailureListener {
                dialog.dismiss()
            }
        }
    }

    private fun uploadData() {
        var nome = tvName.text.toString()
        var sesso = tvSex.text.toString()
        var data = tvDate.text.toString()
        val dataClass = ProfileData(nome, sesso, data, imageURL)

        FirebaseDatabase.getInstance().getReference("UserInfo").child(user!!)
            .setValue(dataClass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, getString(R.string.dataSaved), Toast.LENGTH_SHORT).show()
                }
            }.addOnCompleteListener { e ->

            }
    }
}