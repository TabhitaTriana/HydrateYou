package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PengingatMinum : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengingat_minum)

        // Tombol OK
        val okeButton = findViewById<Button>(R.id.okeoke)
        okeButton.setOnClickListener {
            // Arahkan ke MainActivity atau halaman utama lainnya
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
