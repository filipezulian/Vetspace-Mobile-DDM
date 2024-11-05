package com.example.ddm_vetspace.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ddm_vetspace.R
import com.example.ddm_vetspace.viewmodel.UsuarioViewModel
import com.example.ddm_vetspace.viewmodel.UsuarioViewModelFactory

class Cadastro : AppCompatActivity() {

    private val usuarioViewModel: UsuarioViewModel by viewModels { UsuarioViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val phoneEditText = findViewById<EditText>(R.id.editTextPhone)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                usuarioViewModel.register(name, email, phone, password)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        usuarioViewModel.registerResult.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }.onFailure {
                Toast.makeText(this, "Erro no cadastro: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
