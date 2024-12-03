package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PelacakAirActivity : AppCompatActivity() {

    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pelacak_air)

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase
        val database = FirebaseDatabase.getInstance()
        val userId = "userId123" // Ganti dengan user ID dinamis jika perlu
        userRef = database.getReference("users/$userId/dailyWater")

        // Initialize buttons
        val imageButton1 = findViewById<ImageButton>(R.id.ImageButton1)
        val imageButton2 = findViewById<ImageButton>(R.id.Avatar1)
        val imageButton3 = findViewById<ImageButton>(R.id.ImageButton3)
        val imageButton4 = findViewById<ImageButton>(R.id.ImageButton4)
        val imageButton5 = findViewById<ImageButton>(R.id.ImageButton5)
        val imageButton6 = findViewById<ImageButton>(R.id.ImageButton6)

        // Set click listeners for buttons
        imageButton1.setOnClickListener { sendWaterAmount(25) }
        imageButton2.setOnClickListener { sendWaterAmount(50) }
        imageButton3.setOnClickListener { sendWaterAmount(100) }
        imageButton4.setOnClickListener { sendWaterAmount(200) }
        imageButton5.setOnClickListener { sendWaterAmount(300) }
        imageButton6.setOnClickListener { sendWaterAmount(400) }

        // Initialize BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_water // Pastikan ID ini sesuai dengan menu

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(this, PelacakAirActivity::class.java))
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
                R.id.bottom_water -> true
                R.id.bottom_profile -> {
                    startActivity(Intent(this, Profile::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun sendWaterAmount(amount: Int) {
        // Get current water value
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentWater = snapshot.getValue(Int::class.java) ?: 0
                val updatedWater = currentWater + amount

                // Set updated water value
                userRef.setValue(updatedWater)
                    .addOnSuccessListener {
                        // Redirect to MainActivity
                        val intent = Intent(this@PelacakAirActivity, MainActivity::class.java)
                        intent.putExtra("EXTRA_WATER_AMOUNT", amount)
                        startActivity(intent)
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }
}
