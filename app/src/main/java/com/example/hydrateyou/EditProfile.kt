package com.example.hydrateyou

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfile : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null  // Menyimpan Uri gambar yang dipilih

    private lateinit var namaEditText: EditText
    private lateinit var usiaEditText: EditText
    private lateinit var beratEditText: EditText
    private lateinit var tinggiEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Menghubungkan komponen UI ke variabel
        namaEditText = findViewById(R.id.edit_nama)
        usiaEditText = findViewById(R.id.edit_usia)
        beratEditText = findViewById(R.id.edit_berat)
        tinggiEditText = findViewById(R.id.edit_tinggi)
        val selesaiButton = findViewById<Button>(R.id.selesai)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val profileImageView = findViewById<ImageView>(R.id.profile_img) // ImageView untuk menampilkan gambar profil
        val pilihGambarButton = findViewById<ImageButton>(R.id.profile_btn) // Tombol pilih gambar

        // Menghubungkan ke Firestore untuk memuat data pengguna
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            db.collection("users").document(currentUserId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userProfile = document.toObject(UserProfile::class.java)
                        namaEditText.setText(userProfile?.nama)
                        usiaEditText.setText(userProfile?.usia?.toString())
                        beratEditText.setText(userProfile?.beratBadan?.toString())
                        tinggiEditText.setText(userProfile?.tinggiBadan?.toString())

                        // Menampilkan gambar profil jika ada
                        Glide.with(this)
                            .load(userProfile?.fotoProfil ?: R.drawable.ic_account) // Menampilkan gambar default jika tidak ada URL
                            .into(profileImageView)

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memuat data!", Toast.LENGTH_SHORT).show()
                }
        }

        // Menambahkan fungsionalitas untuk memilih gambar
        pilihGambarButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Menangani hasil pemilihan gambar
        selesaiButton.setOnClickListener {
            selesaiButtonClicked(profileImageView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            val profileImageView = findViewById<ImageView>(R.id.profile_img) // Pastikan ini sesuai dengan ID yang kamu pakai
            profileImageView.setImageURI(imageUri) // Menampilkan gambar yang dipilih
        }
    }

    // Fungsi untuk menyimpan data profil di Firestore
    private fun saveProfileData(photoUrl: String, updatedNama: String, usia: Int, berat: Int, tinggi: Int, email: String) {
        val userProfile = UserProfile(updatedNama, berat, tinggi, usia, email, photoUrl)

        auth.currentUser?.uid?.let {
            db.collection("users").document(it).set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Profile::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memperbarui profil!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Fungsi untuk upload gambar ke Firebase Storage
    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val fileReference = storageReference.child("profile_pictures/${auth.currentUser?.uid}.jpg")

        fileReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Mendapatkan URL gambar yang di-upload
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
                    val photoUrl = downloadUri.toString()

                    // Menyimpan data ke Firestore, termasuk URL gambar
                    saveProfileData(photoUrl, namaEditText.text.toString().trim(), usiaEditText.text.toString().toInt(), beratEditText.text.toString().toInt(), tinggiEditText.text.toString().toInt(), auth.currentUser?.email ?: "")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal meng-upload gambar", Toast.LENGTH_SHORT).show()
            }
    }

    // Menangani tombol selesai untuk mengupdate data
    fun selesaiButtonClicked(profileImageView: ImageView) {
        val updatedNama = namaEditText.text.toString().trim()
        val updatedUsia = usiaEditText.text.toString().trim()
        val updatedBerat = beratEditText.text.toString().trim()
        val updatedTinggi = tinggiEditText.text.toString().trim()

        if (updatedNama.isNotEmpty() && updatedUsia.isNotEmpty() && updatedBerat.isNotEmpty() && updatedTinggi.isNotEmpty()) {
            val usia = updatedUsia.toIntOrNull()
            val berat = updatedBerat.toIntOrNull()
            val tinggi = updatedTinggi.toIntOrNull()

            if (usia != null && berat != null && tinggi != null) {
                // Ambil email dari Firebase Authentication
                val currentUser = auth.currentUser
                val email = currentUser?.email ?: "" // Ambil email

                if (imageUri != null) {
                    // Upload gambar profil jika ada
                    uploadImageToFirebase(imageUri!!)
                } else {
                    // Jika tidak ada gambar, lanjutkan update tanpa gambar
                    saveProfileData("", updatedNama, usia, berat, tinggi, email)
                }
            } else {
                Toast.makeText(this, "Pastikan usia, berat, dan tinggi adalah angka!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Harap lengkapi semua data!", Toast.LENGTH_SHORT).show()
        }
    }
}
