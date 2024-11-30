package com.example.hydrateyou

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Ambil komponen dari layout
        val namaEditText = findViewById<EditText>(R.id.edit_nama)
        val emailEditText = findViewById<EditText>(R.id.edit_email)
        val passwordEditText = findViewById<EditText>(R.id.edit_pass)
        val rePasswordEditText = findViewById<EditText>(R.id.edit_re_pass)
        val daftarButton = findViewById<Button>(R.id.daftar)

        // Tindakan saat klik Daftar button
        daftarButton.setOnClickListener {
            val nama = namaEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val rePassword = rePasswordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && password == rePassword) {
                // Registrasi pengguna dengan Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Pengguna berhasil terdaftar
                            val user = auth.currentUser
                            Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()
                            // Arahkan ke halaman login setelah berhasil daftar
                            startActivity(Intent(this, Login::class.java))
                            finish()
                        } else {
                            // Jika gagal
                            Toast.makeText(this, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                if (password != rePassword) {
                    Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
