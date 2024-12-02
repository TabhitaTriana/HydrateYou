package com.example.hydrateyou

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChallengeActivity : AppCompatActivity() {

    private var currentWaterIntake: Int = 0 // Total konsumsi air saat ini (dalam mililiter)
    private val dailyGoal: Int = 2000 // Target konsumsi harian (dalam mililiter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

        // Referensi ke view dari XML
        val progressBar: ProgressBar = findViewById(R.id.daily_progress)
        val waterIntakeText: TextView = findViewById(R.id.water_intake)
        val addWaterButton: Button = findViewById(R.id.add_water_button)

        // Inisialisasi tampilan
        updateProgress(progressBar, waterIntakeText)

        // Aksi ketika tombol ditekan
        addWaterButton.setOnClickListener {
            addWater(250, progressBar, waterIntakeText) // Tambah 250ml air
        }
    }

    private fun updateProgress(progressBar: ProgressBar, waterIntakeText: TextView) {
        // Hitung persentase konsumsi air
        val progressPercentage = (currentWaterIntake * 100) / dailyGoal

        // Update progress bar
        progressBar.progress = progressPercentage

        // Update teks konsumsi air
        waterIntakeText.text = "${currentWaterIntake / 1000.0}L dari ${dailyGoal / 1000.0}L"
    }

    private fun addWater(amount: Int, progressBar: ProgressBar, waterIntakeText: TextView) {
        // Tambahkan konsumsi air
        currentWaterIntake += amount

        // Jika konsumsi air melebihi target
        if (currentWaterIntake >= dailyGoal) {
            Toast.makeText(this, "Selamat! Anda mencapai target harian!", Toast.LENGTH_SHORT).show()
        }

        // Update tampilan
        updateProgress(progressBar, waterIntakeText)
    }
}
