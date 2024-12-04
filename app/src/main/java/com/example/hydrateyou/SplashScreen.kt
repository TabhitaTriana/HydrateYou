package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Cek status login dari SharedPreferences
        val sharedPref = getSharedPreferences("HydrateYouPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        // Delay selama 2 detik sebelum pindah ke activity berikutnya
        android.os.Handler().postDelayed({
            val nextActivity = if (isLoggedIn) {
                PengingatMinum::class.java // Jika sudah login, langsung ke PengingatMinum
            } else {
                Login::class.java // Jika belum login, arahkan ke Login
            }
            startActivity(Intent(this, nextActivity))
            finish() // Menutup SplashScreen agar tidak bisa kembali
        }, 2000)
    }
}
