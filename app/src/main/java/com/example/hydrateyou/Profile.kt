package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

                        // Menampilkan gambar profil jika ada
                        Glide.with(this)
                            .load(userProfile?.fotoProfil)
                            .into(profileImageView)
                    } else {
                        Log.e("Profile", "Dokumen tidak ditemukan")
                        namaTextView.text = "Gagal memuat data"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Profile", "Gagal memuat data", exception)
                    namaTextView.text = "Gagal memuat data"
                }
        } else {
            Log.e("Profile", "User ID tidak ditemukan")
            namaTextView.text = "Tidak ada pengguna yang login"
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
