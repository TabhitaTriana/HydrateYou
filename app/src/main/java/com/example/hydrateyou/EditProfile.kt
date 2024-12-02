package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Menangani padding untuk inset sistem (status bar dan navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Ambil data dari intent
        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")

        // Inisialisasi komponen layout
        val namaEditText = findViewById<EditText>(R.id.edit_nama)
        val usiaEditText = findViewById<EditText>(R.id.edit_usia)
        val beratEditText = findViewById<EditText>(R.id.edit_berat)
        val tinggiEditText = findViewById<EditText>(R.id.edit_tinggi)
        val selesaiButton = findViewById<Button>(R.id.selesai)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Isi nama dari intent jika ada
        namaEditText.setText(nama)

        // Fungsi tombol "Back"
        backButton.setOnClickListener {
            // Kembali ke SignUp jika pengguna belum selesai
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        // Fungsi tombol "Selesai"
        selesaiButton.setOnClickListener {
            val updatedNama = namaEditText.text.toString().trim()
            val updatedUsia = usiaEditText.text.toString().trim()
            val updatedBerat = beratEditText.text.toString().trim()
            val updatedTinggi = tinggiEditText.text.toString().trim()

            // Validasi input
            if (updatedNama.isNotEmpty() && updatedUsia.isNotEmpty() && updatedBerat.isNotEmpty() && updatedTinggi.isNotEmpty()) {
                // Simpan data ke database (contoh menggunakan Toast untuk sementara)
                Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()

                // Arahkan ke halaman Login
                startActivity(Intent(this, Login::class.java))
                finish()
            } else {
                Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
