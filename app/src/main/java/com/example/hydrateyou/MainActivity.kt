package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.google.firebase.FirebaseApp
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

        // Mengatur padding untuk menghindari sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val userId = "userId123"
        val userRef = database.getReference("users/$userId/dailyWater")

        // Inisialisasi views
        progressBar = findViewById(R.id.progressBar)
        waterTextView = findViewById(R.id.waterTextView)
        progressBar.max = maxWater

        // Membaca data dari Firebase
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dailyWater = dataSnapshot.getValue(Int::class.java) ?: 0
                updateWaterProgress(dailyWater)
                Log.d("Firebase", "Jumlah air yang dikonsumsi hari ini: $dailyWater")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read value.", error.toException())
            }
        })

        // Cek apakah ada tambahan air dari intent
        val extraWaterAmount = intent.getIntExtra("EXTRA_WATER_AMOUNT", 0)
        if (extraWaterAmount > 0) {
            updateWaterProgress(extraWaterAmount)
        }

        // Menyiapkan BarChart
        barChart = findViewById(R.id.chartKonsumBulanan)
        val entries = ArrayList<BarEntry>().apply {
            add(BarEntry(0f, 3f))
            add(BarEntry(1f, 4f))
            add(BarEntry(2f, 6f))
        }

        val dataSet = BarDataSet(entries, "Konsumsi Air Bulanan")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val data = BarData(dataSet)
        barChart.data = data
        barChart.invalidate()

        // Menyiapkan BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_home // Pastikan ID ini sesuai dengan menu

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> true
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

    private fun updateWaterProgress(additionalWater: Int) {
        currentWater += additionalWater
        if (currentWater > maxWater) {
            currentWater = maxWater
        }
        progressBar.progress = currentWater
        waterTextView.text = "$currentWater ml / $maxWater ml"
    }
}
