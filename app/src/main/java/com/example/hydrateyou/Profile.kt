package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Profile : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // Menghubungkan button ke variabel
        val editBtn = findViewById<Button>(R.id.edit_btn) // Sesuaikan dengan tipe button yang benar
        val kebijakanBtn = findViewById<ImageButton>(R.id.kebijakan_btn)
        val keluarBtn = findViewById<ImageButton>(R.id.keluar_btn)

        // Menambahkan klik listener untuk editBtn
        editBtn.setOnClickListener {
            // Intent untuk berpindah ke EditProfile Activity
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // Menambahkan klik listener untuk kebijakanBtn
        kebijakanBtn.setOnClickListener {
            // Intent untuk berpindah ke KebijakanPrivasi Activity
            val intent = Intent(this, KebijakanPrivasi::class.java)
            startActivity(intent)
        }

        // Menambahkan klik listener untuk keluarBtn
        keluarBtn.setOnClickListener {
            // Intent untuk berpindah ke Login Activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bottom Navigation setup
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_profile // Select the profile item by default

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_information -> {
                    startActivity(Intent(this, Information::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_challenge -> {
                    startActivity(Intent(this, KebijakanPrivasi::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_water -> {
                    startActivity(Intent(this, PelacakAirActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_profile -> true // Already on this screen
                else -> false
            }
        }
    }
}
