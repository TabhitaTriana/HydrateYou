package com.example.hydrateyou

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
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

        // Initialize Firebase
        val database = FirebaseDatabase.getInstance()
        val userId = "userId123"
        val userRef = database.getReference("users/$userId/dailyWater")

        // Initialize views
        progressBar = findViewById(R.id.progressBar)
        waterTextView = findViewById(R.id.waterTextView)
        progressBar.max = maxWater

        // Write a message to the database
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dailyWater = dataSnapshot.getValue(Int::class.java)
                Log.d("Firebase", "Jumlah air yang dikonsumsi hari ini: $dailyWater")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read value.", error.toException())
            }
        })

        val extraWaterAmount = intent.getIntExtra("EXTRA_WATER_AMOUNT", 0)
        if (extraWaterAmount > 0){
            updateWaterProgress(extraWaterAmount)
        }

        barChart = findViewById(R.id.chartKonsumBulanan);
        barChart.visibility = View.VISIBLE

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 3f))
        entries.add(BarEntry(1f, 4f))
        entries.add(BarEntry(3f, 6f))

        Log.d("BarChart", "Data entries: $entries")

        val dataSet = BarDataSet(entries, "Konsumsi Air Bulanan")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val data = BarData(dataSet)
        barChart.data = data
        barChart.invalidate()

        progressBar = findViewById(R.id.progressBar)
        waterTextView = findViewById(R.id.waterTextView)
        progressBar.max = maxWater

        FirebaseApp.initializeApp(this)
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
