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
        val infoBtn = findViewById<ImageButton>(R.id.info_btn)
        val kebijakanBtn = findViewById<ImageButton>(R.id.kebijakan_btn)
        val keluarBtn = findViewById<ImageButton>(R.id.keluar_btn)
        val namaTextView = findViewById<TextView>(R.id.tv_user)
        val beratTextView = findViewById<TextView>(R.id.tv_berat)
        val tinggiTextView = findViewById<TextView>(R.id.tv_tinggi)
        val usiaTextView = findViewById<TextView>(R.id.tv_usia)
        val emailTextView = findViewById<TextView>(R.id.tv_email)
        val profileImageView = findViewById<ImageView>(R.id.account)

        // Menyiapkan BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_profile // Pastikan ID ini sesuai dengan menu

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> navigateTo(MainActivity::class.java)
                R.id.bottom_information -> navigateTo(Information::class.java)
                R.id.bottom_challenge -> navigateTo(ChallengeActivity::class.java)
                R.id.bottom_water -> navigateTo(PelacakAirActivity::class.java)
                R.id.bottom_profile -> true
                else -> false
            }
        }

        val currentUserId = auth.currentUser?.uid
        currentUserId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userProfile = document.toObject(UserProfile::class.java)
                        namaTextView.text = userProfile?.nama ?: "Nama tidak tersedia"
                        usiaTextView.text = userProfile?.usia?.let { "$it Tahun" } ?: "-"
                        beratTextView.text = userProfile?.beratBadan?.let { "$it Kg" } ?: "-"
                        tinggiTextView.text = userProfile?.tinggiBadan?.let { "$it cm" } ?: "-"
                        emailTextView.text = userProfile?.email ?: "Email tidak tersedia"

                        // Menampilkan gambar profil
                        userProfile?.fotoProfil?.let { imagePath ->
                            val file = File(imagePath)
                            if (file.exists()) {
                                Glide.with(this)
                                    .load(file)
                                    .circleCrop() // Membuat gambar bulat
                                    .into(profileImageView)
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    namaTextView.text = "Gagal memuat data"
                }
        }

        // Tombol Edit Profil
        editBtn.setOnClickListener {
            navigateTo(EditProfile::class.java)
        }

        infoBtn.setOnClickListener {
            navigateTo(InfoPribadi::class.java)
        }

        // Tombol Kebijakan Privasi
        kebijakanBtn.setOnClickListener {
            navigateTo(KebijakanPrivasi::class.java)

        }// Tombol Kebijakan Privasi
        kebijakanBtn.setOnClickListener {
            navigateTo(KebijakanPrivasi::class.java)
        }

        // Tombol Keluar
        keluarBtn.setOnClickListener {
            auth.signOut()
            navigateTo(Login::class.java, true)
        }
    }

    private fun navigateTo(activityClass: Class<*>, finish: Boolean = false): Boolean {
        startActivity(Intent(this, activityClass))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        if (finish) finish()
        return true // Return true to indicate successful navigation
    }
}
