package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class PelacakAirActivity : AppCompatActivity() {

    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelacak_air)

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Cek apakah pengguna sudah login
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            navigateTo(Login::class.java, true)
            return
        }

        // Initialize Firebase reference
        val database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        userRef = database.getReference("users/$userId")

        // Initialize buttons
        val buttonAmounts = mapOf(
            R.id.ImageButton1 to 25,
            R.id.Avatar1 to 50,
            R.id.ImageButton3 to 100,
            R.id.ImageButton4 to 200,
            R.id.ImageButton5 to 300,
            R.id.ImageButton6 to 400
        )

        buttonAmounts.forEach { (buttonId, amount) ->
            findViewById<ImageButton>(buttonId).setOnClickListener {
                sendWaterAmount(amount)
            }
        }

        val tvDateToday: TextView = findViewById(R.id.tvDateToday)

        // Dapatkan tanggal hari ini
        val calendar = Calendar.getInstance()
        val dateFormatToday = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        val todayDate = dateFormatToday.format(calendar.time)

        // Tampilkan di TextView
        tvDateToday.text = todayDate

        val buttonIds = listOf(
            R.id.button_tanggal,
            R.id.button_tanggal2,
            R.id.button_tanggal3,
            R.id.button_tanggal4,
            R.id.button_tanggal5
        )

        // Format tanggal
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

        // Mengisi tombol dengan tanggal
        val calendarForButtons = Calendar.getInstance()
        buttonIds.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.text = dateFormat.format(calendarForButtons.time)
            calendarForButtons.add(Calendar.DAY_OF_YEAR, 1) // Tambah 1 hari
        }

        // Initialize BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_water

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> navigateTo(MainActivity::class.java)
                R.id.bottom_information -> navigateTo(Information::class.java)
                R.id.bottom_challenge -> navigateTo(ChallengeActivity::class.java)
                R.id.bottom_water -> true
                R.id.bottom_profile -> navigateTo(Profile::class.java)
                else -> false
            }
        }
    }

    private fun sendWaterAmount(amount: Int) {
        userRef.child("dailyWater").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentWater = snapshot.getValue(Long::class.java)?.toInt() ?: 0
                val updatedWater = currentWater + amount

                userRef.child("dailyWater").setValue(updatedWater)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Successfully updated water amount: $updatedWater")
                        val intent = Intent(this@PelacakAirActivity, MainActivity::class.java)
                        intent.putExtra("EXTRA_WATER_AMOUNT", updatedWater)
                        startActivity(intent)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firebase", "Failed to update water amount", exception)
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read value.", error.toException())
            }
        })
    }

    private fun navigateTo(activityClass: Class<*>, finish: Boolean = false): Boolean {
        startActivity(Intent(this, activityClass))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        if (finish) finish()
        return true
    }
}
