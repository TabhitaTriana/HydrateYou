package com.example.hydrateyou

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class EditProfile : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null // Store selected image URI

    private lateinit var namaEditText: EditText
    private lateinit var usiaEditText: EditText
    private lateinit var beratEditText: EditText
    private lateinit var tinggiEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Bind UI components to variables
        namaEditText = findViewById(R.id.edit_nama)
        usiaEditText = findViewById(R.id.edit_usia)
        beratEditText = findViewById(R.id.edit_berat)
        tinggiEditText = findViewById(R.id.edit_tinggi)
        val selesaiButton = findViewById<Button>(R.id.selesai)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val profileImageView = findViewById<ImageView>(R.id.profile_img)
        val pilihGambarButton = findViewById<ImageButton>(R.id.profile_btn)

        // Load user data from Firestore
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            db.collection("users").document(currentUserId).get()
                .addOnSuccessListener { document ->
                    document?.toObject(UserProfile::class.java)?.let { userProfile ->
                        namaEditText.setText(userProfile.nama)
                        usiaEditText.setText(userProfile.usia.toString())
                        beratEditText.setText(userProfile.beratBadan.toString())
                        tinggiEditText.setText(userProfile.tinggiBadan.toString())

                        // Load profile image
                        val imagePath = userProfile.fotoProfil
                        val imageFile = File(imagePath ?: "")
                        if (imageFile.exists()) {
                            Glide.with(this)
                                .load(imageFile)
                                .circleCrop()
                                .into(profileImageView)
                        } else {
                            Glide.with(this)
                                .load(R.drawable.ic_account) // Default image
                                .circleCrop()
                                .into(profileImageView)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load user data!", Toast.LENGTH_SHORT).show()
                }
        }

        // Handle image selection
        pilihGambarButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Handle save button click
        selesaiButton.setOnClickListener {
            saveProfileData(profileImageView)
        }
    }

    // Handle image selection result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data?.data != null) {
            imageUri = data.data
            val profileImageView = findViewById<ImageView>(R.id.profile_img)
            Glide.with(this)
                .load(imageUri)
                .circleCrop()
                .into(profileImageView)
        }
    }

    // Save profile data to Firestore
    private fun saveProfileData(profileImageView: ImageView) {
        val updatedNama = namaEditText.text.toString().trim()
        val updatedUsia = usiaEditText.text.toString().trim()
        val updatedBerat = beratEditText.text.toString().trim()
        val updatedTinggi = tinggiEditText.text.toString().trim()

        if (updatedNama.isNotEmpty() && updatedUsia.isNotEmpty() && updatedBerat.isNotEmpty() && updatedTinggi.isNotEmpty()) {
            val usia = updatedUsia.toIntOrNull()
            val berat = updatedBerat.toIntOrNull()
            val tinggi = updatedTinggi.toIntOrNull()

            if (usia != null && berat != null && tinggi != null) {
                val email = auth.currentUser?.email ?: ""

                if (imageUri != null) {
                    val imagePath = saveImageLocally(imageUri!!)
                    if (imagePath != null) {
                        saveProfileDataToFirestore(imagePath, updatedNama, usia, berat, tinggi, email)
                    } else {
                        Toast.makeText(this, "Failed to save image!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    saveProfileDataToFirestore("", updatedNama, usia, berat, tinggi, email)
                }
            } else {
                Toast.makeText(this, "Age, weight, and height must be numbers!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
        }
    }

    // Save user profile data to Firestore
    private fun saveProfileDataToFirestore(photoPath: String, updatedNama: String, usia: Int, berat: Int, tinggi: Int, email: String) {
        val userProfile = UserProfile(updatedNama, berat, tinggi, usia, email, photoPath)

        auth.currentUser?.uid?.let {
            db.collection("users").document(it).set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Profile::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Save the image locally
    private fun saveImageLocally(imageUri: Uri): String? {
        val fileName = "${auth.currentUser?.uid}_profile.jpg"
        val file = File(filesDir, fileName)

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val outputStream: OutputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}
