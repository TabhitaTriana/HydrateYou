package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        // Tombol klaim reward
        val reward1Button: Button = findViewById(R.id.reward1)
        val reward2Button: Button = findViewById(R.id.reward2)
        val reward3Button: Button = findViewById(R.id.reward3)

        reward1Button.setOnClickListener {
            navigateToReward(1)
        }

        reward2Button.setOnClickListener {
            navigateToReward(2)
        }

        reward3Button.setOnClickListener {
            navigateToReward(3)
        }

        // Menyiapkan BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_challenge // Pastikan ID ini sesuai dengan menu

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(this, Profile::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                R.id.bottom_information -> {
                    startActivity(Intent(this, Information::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                R.id.bottom_challenge -> true
                R.id.bottom_water -> {
                    startActivity(Intent(this, PelacakAirActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                R.id.bottom_profile -> {
                    startActivity(Intent(this, Profile::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                    true
                }
                else -> false
            }
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

    private fun navigateToReward(rewardId: Int) {
        val intent = Intent(this, RewardActivity::class.java)
        intent.putExtra("reward_id", rewardId) // Kirim data ID reward ke RewardActivity
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}

