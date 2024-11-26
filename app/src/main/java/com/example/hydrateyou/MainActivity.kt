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

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var waterTextView: TextView
    private lateinit var barChart: BarChart

    private var currentWater: Int = 0
    private val maxWater: Int = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        progressBar.max = 2000

        // menerima data dari PelacakAirActivity
        val additionalWater = intent.getIntExtra("EXTRA_WATER_AMOUNT", 0)

        // update progress bar
        val currentProgress = progressBar.progress
        progressBar.progress = (currentProgress + additionalWater).coerceAtMost(progressBar.max)

        progressBar = findViewById(R.id.progressBar)
        waterTextView = findViewById(R.id.waterTextView)

        progressBar.max = maxWater
        // cek data yg dikirim dari activity pelacak air activity
        val extraWaterAmount = intent.getIntExtra("EXTRA_WATER_AMOUNT", 0)
        if (extraWaterAmount > 0) {
            updateWaterProgress(extraWaterAmount)
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
