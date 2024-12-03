package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hydrateyou.R

class RewardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        // Referensi ke elemen UI
        val medalCircle: ImageView = findViewById(R.id.medal_circle)
        val starIcon: ImageView = findViewById(R.id.star_icon)
        val ribbon: View = findViewById(R.id.ribbon)
        val congratsText: TextView = findViewById(R.id.congrats_text)
        val pointsText: TextView = findViewById(R.id.points_text)
        val shareButton: Button = findViewById(R.id.share_button)

        // Ambil tipe reward dari intent
        val rewardType = intent.getIntExtra("REWARD_TYPE", 1) // Default ke reward 1

        // Set elemen UI sesuai tipe reward
        when (rewardType) {
            1 -> {
                // Reward 1
                medalCircle.setBackgroundResource(R.drawable.reward_bg1)
                starIcon.setBackgroundResource(R.drawable.medal_1)
                ribbon.setBackgroundResource(R.drawable.background_reward1)
                congratsText.text = "SELAMAT!"
                pointsText.text = "Selamat kamu mendapatkan 2000 point!"
            }
            2 -> {
                // Reward 2
                medalCircle.setBackgroundResource(R.drawable.reward_bg2)
                starIcon.setBackgroundResource(R.drawable.medal_2)
                ribbon.setBackgroundResource(R.drawable.background_reward2)
                congratsText.text = "KAMU LUAR BIASA!"
                pointsText.text = "Kamu berhasil mendapatkan 3000 point!"
            }
            3 -> {
                // Reward 3
                medalCircle.setBackgroundResource(R.drawable.reward_bg3)
                starIcon.setBackgroundResource(R.drawable.medal_3)
                ribbon.setBackgroundResource(R.drawable.background_reward3)
                congratsText.text = "SANGAT HEBAT!"
                pointsText.text = "Kamu mendapatkan 5000 point! Terus lanjutkan!"
            }
        }

        // Aksi untuk tombol Share
        shareButton.setOnClickListener {
            shareReward(rewardType)
        }
    }

    private fun shareReward(rewardType: Int) {
        val message = when (rewardType) {
            1 -> "Saya mendapatkan reward 1 dengan 2000 point di aplikasi ini!"
            2 -> "Saya mendapatkan reward 2 dengan 3000 point di aplikasi ini!"
            3 -> "Saya mendapatkan reward 3 dengan 5000 point di aplikasi ini!"
            else -> "Saya mendapatkan reward luar biasa di aplikasi ini!"
        }
        // Kirimkan intent share
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan Reward"))
    }
}
