package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SplashScreen", "onCreate called")
        setContentView(R.layout.activity_splash_screen)

        // Menambahkan delay selama 2 detik sebelum pindah ke Login Activity
        Handler().postDelayed({
            // Menavigasi ke Login Activity setelah splash screen
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Menutup SplashScreen activity agar tidak bisa kembali ke halaman SplashScreen
        }, 2000) // 2000ms = 2 detik
    }
}

