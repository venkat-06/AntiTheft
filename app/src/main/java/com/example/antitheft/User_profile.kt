package com.example.antitheft

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.location.Location
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.io.ByteArrayOutputStream


class User_profile : AppCompatActivity() {

    private lateinit var edt_uname: EditText

    private lateinit var edt_phoneNum: EditText
    private lateinit var edt_loc: EditText
    private lateinit var edt_email: EditText

    private lateinit var btn_updateUser: Button

    private val PICK_IMAGE = 1

    private lateinit var imgVTxt: TextView

    private lateinit var databaseReference: DatabaseReference

    private lateinit var uID: String
    private lateinit var selectedImageUri: Uri
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val selectImageButton = findViewById<ImageView>(R.id.imageProfile)
        selectImageButton.setOnClickListener {
            openImageChooser()
        }

        edt_uname = findViewById(R.id.edt_c_uname)

        edt_phoneNum = findViewById(R.id.edt_c_phoneNum)

        edt_email = findViewById(R.id.edt_c_email)

        edt_loc = findViewById(R.id.edt_c_loc)
        btn_updateUser = findViewById(R.id.btn_updateUser)
        edt_email = findViewById(R.id.edt_c_email)

        imgVTxt = findViewById(R.id.myImageViewText)

        fetchUserData()
        /*val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {

            val userEmail = currentUser.email
            email.text = userEmail

        }*/
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        btn_updateUser.setOnClickListener {
            val uName = edt_uname.text.toString()
            val uNum = edt_phoneNum.text.toString()
            val uLoc = edt_loc.text.toString()
            val mail = edt_email.text.toString()
            if (uNum.length != 10) {
                Toast.makeText(this, "Incorrect Mobile Number", Toast.LENGTH_SHORT).show()
            } else {


                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val user = UserData(uName, uNum, mail, uLoc)


                if (userId != null) {
                    databaseReference.child(userId).setValue(user)
                        .addOnSuccessListener {
                            // Data written successfully
                            Toast.makeText(
                                this,
                                "User details updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, Main_Activity::class.java)

                            startActivity(intent)


                        }
                        .addOnFailureListener { e ->
                            // Error writing data
                            Log.e("User_profile", "Error updating user details", e)
                            Toast.makeText(
                                this,
                                "Failed to update user details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Log.e("User_profile", "User ID is null")
                }


            }
        }


    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.let {
                selectedImageUri = it.data!!
                val intent = Intent(this, Main_Activity::class.java)
                intent.putExtra("imageUri", selectedImageUri.toString())
                startActivity(intent)
                // Save the selectedImageUri to SharedPreferences or any other storage method
            }
        }
    }

    private fun fetchUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId.toString())
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(UserData::class.java)
                    user?.let {
                        // Populate EditText fields with user data
                        edt_uname.setText(it.userName)

                        edt_phoneNum.setText(it.userNumber)
                        edt_loc.setText(it.userLoc)
                    }
                } else {
                    Log.d("User_profile", "No data found for user: $userId")
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("User_profile", "Error fetching user data", databaseError.toException())
            }
        })
    }




}