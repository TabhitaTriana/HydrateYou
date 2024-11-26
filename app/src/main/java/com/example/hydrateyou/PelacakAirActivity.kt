package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PelacakAirActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pelacak_air)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imageButton1 = findViewById<ImageButton>(R.id.ImageButton1)
        val imageButton2 = findViewById<ImageButton>(R.id.Avatar1)
        val imageButton3 = findViewById<ImageButton>(R.id.ImageButton3)
        val imageButton4 = findViewById<ImageButton>(R.id.ImageButton4)
        val imageButton5 = findViewById<ImageButton>(R.id.ImageButton5)
        val imageButton6 = findViewById<ImageButton>(R.id.ImageButton6)

        imageButton1.setOnClickListener {
            sendWaterAmount(25)
        }
        imageButton2.setOnClickListener {
            sendWaterAmount(50)
        }
        imageButton3.setOnClickListener {
            sendWaterAmount(100)
        }
        imageButton4.setOnClickListener {
            sendWaterAmount(200)
        }
        imageButton5.setOnClickListener {
            sendWaterAmount(300)
        }
        imageButton6.setOnClickListener {
            sendWaterAmount(400)
        }

    }
    private fun sendWaterAmount(amount: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("EXTRA_WATER_AMOUNT", amount)
        startActivity(intent)
    }
}
