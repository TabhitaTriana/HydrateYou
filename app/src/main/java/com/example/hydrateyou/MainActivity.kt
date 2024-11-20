package com.example.hydrateyou

import android.os.Bundle
import android.widget.ProgressBar
import android.view.View
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)

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
    }
}
