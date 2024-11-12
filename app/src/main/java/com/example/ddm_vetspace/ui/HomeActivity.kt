package com.example.ddm_vetspace.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ddm_vetspace.R
import com.example.ddm_vetspace.database.background_service_pet
import com.example.ddm_vetspace.viewmodel.UsuarioViewModel
import com.example.ddm_vetspace.viewmodel.UsuarioViewModelFactory

class HomeActivity : AppCompatActivity() {

    private val usuarioViewModel: UsuarioViewModel by viewModels { UsuarioViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                usuarioViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }

        val cadastroText = findViewById<TextView>(R.id.registerText)
        cadastroText.setOnClickListener {
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }
    }

        usuarioViewModel.loginResult.observe(this, Observer { result ->
            result.onSuccess { usuario ->
                saveUserId(usuario.user_id)
                val petWorkRequest = OneTimeWorkRequestBuilder<background_service_pet>().build()
                WorkManager.getInstance(this).enqueue(petWorkRequest)
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Blogs::class.java))
                finish()
            }.onFailure {
                Toast.makeText(this, "Erro no login: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })

        usuarioViewModel.errorMessage.observe(this, Observer { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
    }

    private fun saveUserId(userId: Int) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putInt("user_id", userId)
            .apply()
    }
}
