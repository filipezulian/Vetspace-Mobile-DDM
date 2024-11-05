package com.example.ddm_vetspace

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ddm_vetspace.dto.CadastroRequest
import com.example.ddm_vetspace.repository.UsuarioRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Cadastro : AppCompatActivity() {

    private lateinit var repository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // Inicializando o repositório com a instância do Retrofit
        repository = UsuarioRepository(RetrofitInitializer.usuarioApi)

        val loginText = findViewById<TextView>(R.id.textViewLogin)
        loginText.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

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
                // Realiza o cadastro usando a função do repositório
                lifecycleScope.launch {
                    val result = repository.cadastrar(name, email, phone, password)
                    result.onSuccess {
                        Toast.makeText(this@Cadastro, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        // Redireciona para a tela de login
                        startActivity(Intent(this@Cadastro, HomeActivity::class.java))
                        finish()
                    }.onFailure {
                        Toast.makeText(this@Cadastro, "Erro no cadastro: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}