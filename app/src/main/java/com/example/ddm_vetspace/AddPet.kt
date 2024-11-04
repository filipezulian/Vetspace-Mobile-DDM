package com.example.ddm_vetspace

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddPet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, adicionarPet::class.java)
            startActivity(intent)
        }

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

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedGender = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@AddPet, "Selected Gender: $selectedGender", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

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
