package com.example.hydrateyou

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InfoPribadi : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var editNama: EditText
    private lateinit var editUsia: EditText
    private lateinit var editBerat: EditText
    private lateinit var editTinggi: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_pribadi)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Bind UI components
        editNama = findViewById(R.id.edit_nama)
        editUsia = findViewById(R.id.edit_usia)
        editBerat = findViewById(R.id.edit_berat)
        editTinggi = findViewById(R.id.edit_tinggi)
        profileImageView = findViewById(R.id.profile_img)
        backButton = findViewById(R.id.backButton)

        // Load user data from Firestore
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            db.collection("users").document(currentUserId).get()
                .addOnSuccessListener { document ->
                    document?.toObject(UserProfile::class.java)?.let { userProfile ->
                        // Update UI with user data
                        editNama.setText(userProfile.nama)
                        editUsia.setText(userProfile.usia.toString())
                        editBerat.setText(userProfile.beratBadan.toString())
                        editTinggi.setText(userProfile.tinggiBadan.toString())

                        // Load profile image
                        val imagePath = userProfile.fotoProfil
                        if (!imagePath.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(imagePath)
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
                    Toast.makeText(this, "Gagal memuat data pengguna!", Toast.LENGTH_SHORT).show()
                }
        }

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Close current activity and return to the previous one
        }
    }
}
