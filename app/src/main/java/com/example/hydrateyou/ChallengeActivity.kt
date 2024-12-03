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

    private var currentWaterIntake: Int = 0 // Current water intake (in milliliters)
    private val dailyGoal: Int = 2000 // Daily water intake goal (in milliliters)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)

        // References to views
        val progressBar: ProgressBar = findViewById(R.id.daily_progress)
        val waterIntakeText: TextView = findViewById(R.id.water_intake)
        val addWaterButton: Button = findViewById(R.id.add_water_button)

        // Initialize progress
        updateProgress(progressBar, waterIntakeText)

        // Add water on button click
        addWaterButton.setOnClickListener {
            addWater(250, progressBar, waterIntakeText) // Add 250ml water
        }

        // Initialize BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_challenge // Make sure this ID matches the menu item ID

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> navigateTo(Profile::class.java)
                R.id.bottom_information -> navigateTo(Information::class.java)
                R.id.bottom_challenge -> true
                R.id.bottom_water -> navigateTo(PelacakAirActivity::class.java)
                R.id.bottom_profile -> navigateTo(Profile::class.java)
                else -> false
            }
        }
    }

    // Function to update progress bar and water intake text
    private fun updateProgress(progressBar: ProgressBar, waterIntakeText: TextView) {
        val progressPercentage = (currentWaterIntake * 100) / dailyGoal
        progressBar.progress = progressPercentage
        waterIntakeText.text = "${currentWaterIntake / 1000.0}L of ${dailyGoal / 1000.0}L"
    }

    // Function to add water intake
    private fun addWater(amount: Int, progressBar: ProgressBar, waterIntakeText: TextView) {
        currentWaterIntake += amount
        if (currentWaterIntake >= dailyGoal) {
            Toast.makeText(this, "Congrats! You've reached your daily goal!", Toast.LENGTH_SHORT).show()
        }
        updateProgress(progressBar, waterIntakeText)
    }

    // Helper function for navigation to other activities
    private fun navigateTo(activityClass: Class<*>, finish: Boolean = false): Boolean {
        startActivity(Intent(this, activityClass))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        if (finish) finish()
        return true
    }
}
