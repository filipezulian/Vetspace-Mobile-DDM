package com.example.ddm_vetspace

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class AddPet : AppCompatActivity() {

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestWritePermissionLauncher: ActivityResultLauncher<String>
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, adicionarPet::class.java)
            startActivity(intent)
        }

        setupSpinners()
        initActivityResultLaunchers()

        val buttonChooseImage = findViewById<Button>(R.id.buttonChooseImage)
        buttonChooseImage.setOnClickListener {
            checkPermissionsAndShowOptions()
        }
    }

    private fun initActivityResultLaunchers() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                findViewById<ImageView>(R.id.imageViewPetIcon).setImageURI(photoUri)
            } else {
                Toast.makeText(this, "Erro ao tirar a foto", Toast.LENGTH_SHORT).show()
            }
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                findViewById<ImageView>(R.id.imageViewPetIcon).setImageURI(it)
            } ?: Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
        }

        requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkWritePermissionAndOpenCamera()
            } else {
                Toast.makeText(this, "É necessário acesso a camera", Toast.LENGTH_SHORT).show()
            }
        }

        requestWritePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(this, "É necessário permissão para salvar foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionsAndShowOptions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            checkWritePermissionAndOpenCamera()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkWritePermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            showImagePickerOptions()
        } else {
            requestWritePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Tirar Foto", "Escolher da Galeria")
        AlertDialog.Builder(this)
            .setTitle("Escolher Imagem")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openCamera() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "pet_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/VetSpace")
        }
        photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (photoUri == null) {
            val photoFile = File(getExternalFilesDir(null), "pet_image_${System.currentTimeMillis()}.jpg")
            photoUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
        }

        if (photoUri != null) {
            takePictureLauncher.launch(photoUri!!)
        } else {
            Toast.makeText(this, "Failed to create a file for the photo.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun setupSpinners() {
        val spinnerGender = findViewById<Spinner>(R.id.spinnerGender)
        val genderOptions = listOf("Masculino", "Feminino")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter

        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val typeOptions = listOf("Cachorro", "Gato")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOptions)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = typeAdapter

        val petIconImageView = findViewById<ImageView>(R.id.imageViewPetIcon)
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedType = parent.getItemAtPosition(position).toString()
                when (selectedType) {
                    "Cachorro" -> petIconImageView.setImageResource(R.drawable.dog)
                    "Gato" -> petIconImageView.setImageResource(R.drawable.cat)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                petIconImageView.setImageResource(R.drawable.dog)
            }
        }
    }
}