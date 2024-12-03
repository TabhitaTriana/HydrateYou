package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var waterTextView: TextView
    private lateinit var barChart: BarChart
    private var currentWater: Int = 0
    private val maxWater: Int = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        // Inisialisasi UI
        progressBar = findViewById(R.id.progressBar)
        waterTextView = findViewById(R.id.waterTextView)
        barChart = findViewById(R.id.chartKonsumBulanan)

        progressBar.max = maxWater

        // Firebase untuk mengambil data air yang diminum
        val userId = currentUser.uid
        val userRef = FirebaseDatabase.getInstance().getReference("users/$userId/dailyWater")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dailyWater = snapshot.getValue(Number::class.java)?.toInt() ?: 0
                updateWaterProgress(dailyWater)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read value.", error.toException())
            }
        })


        // Set chart
        val entries = arrayListOf(
            BarEntry(0f, 3f),
            BarEntry(1f, 4f),
            BarEntry(2f, 6f)
        )
        val dataSet = BarDataSet(entries, "Konsumsi Air Bulanan").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
        }
        barChart.data = BarData(dataSet)
        barChart.invalidate()

        // Bottom Navigation setup
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> true
                R.id.bottom_information -> navigateTo(Information::class.java)
                R.id.bottom_challenge -> navigateTo(ChallengeActivity::class.java)
                R.id.bottom_water -> navigateTo(PelacakAirActivity::class.java)
                R.id.bottom_profile -> navigateTo(Profile::class.java)
                else -> false
            }
        }
    }

    private fun updateWaterProgress(dailyWater: Int) {
        currentWater = dailyWater
        progressBar.progress = dailyWater
        waterTextView.text = "$currentWater / $maxWater ml"
    }

    private fun navigateTo(activityClass: Class<*>, finish: Boolean = false): Boolean {
        startActivity(Intent(this, activityClass))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        if (finish) finish()
        return true // Return true to indicate successful navigation
    }
}
