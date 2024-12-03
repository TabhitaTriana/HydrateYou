package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class Profile : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Menghubungkan komponen UI ke variabel
        val editBtn = findViewById<Button>(R.id.edit_btn)
        val kebijakanBtn = findViewById<ImageButton>(R.id.kebijakan_btn)
        val keluarBtn = findViewById<ImageButton>(R.id.keluar_btn)
        val namaTextView = findViewById<TextView>(R.id.tv_user)
        val beratTextView = findViewById<TextView>(R.id.tv_berat)
        val tinggiTextView = findViewById<TextView>(R.id.tv_tinggi)
        val usiaTextView = findViewById<TextView>(R.id.tv_usia)
        val emailTextView = findViewById<TextView>(R.id.tv_email)
        val profileImageView = findViewById<ImageView>(R.id.account) // ImageView untuk menampilkan foto profil

        // Menyiapkan BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_profile // Pastikan ID ini sesuai dengan menu

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                R.id.bottom_information -> {
                    startActivity(Intent(this, Information::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                R.id.bottom_challenge -> {
                    startActivity(Intent(this, ChallengeActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                R.id.bottom_water -> {
                    startActivity(Intent(this, PelacakAirActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                R.id.bottom_profile -> true
                else -> false
            }
        }

        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            db.collection("users").document(currentUserId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userProfile = document.toObject(UserProfile::class.java)
                        namaTextView.text = userProfile?.nama ?: "Nama tidak tersedia"
                        usiaTextView.text = userProfile?.usia?.let { "$it Tahun" } ?: "-"
                        beratTextView.text = userProfile?.beratBadan?.let { "$it Kg" } ?: "-"
                        tinggiTextView.text = userProfile?.tinggiBadan?.let { "$it cm" } ?: "-"
                        emailTextView.text = userProfile?.email ?: "Email tidak tersedia"

                        // Menampilkan gambar profil dari path lokal jika ada
                        val imagePath = userProfile?.fotoProfil
                        if (!imagePath.isNullOrEmpty()) {
                            val file = File(imagePath)
                            if (file.exists()) {
                                // Menampilkan gambar dengan bentuk bulat
                                Glide.with(this)
                                    .load(file)
                                    .circleCrop() // Memastikan gambar berbentuk bulat
                                    .into(profileImageView)
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    namaTextView.text = "Gagal memuat data"
                }
        }

        // Tombol Edit Profil
        editBtn.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // Tombol Kebijakan Privasi
        kebijakanBtn.setOnClickListener {
            val intent = Intent(this, KebijakanPrivasi::class.java)
            startActivity(intent)
        }

        // Tombol Keluar
        keluarBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}
