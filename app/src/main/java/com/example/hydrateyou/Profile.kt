package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.Button // Gunakan tipe yang sesuai
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Profile : AppCompatActivity() {
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
