package com.example.ddm_vetspace

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ddm_vetspace.dto.LoginRequest
import com.example.ddm_vetspace.model.Usuario
import com.example.ddm_vetspace.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializando componentes da interface do usuário
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // Configura o comportamento do botão de login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = passwordEditText.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                val loginRequest = LoginRequest(email, senha)
                realizarLogin(loginRequest)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para realizar a chamada de login na API
    private fun realizarLogin(loginRequest: LoginRequest) {
        val call = RetrofitInitializer.usuarioApi.autenticar(loginRequest)

        // Executa a chamada de forma assíncrona
        call.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    val usuario = response.body()
                    Toast.makeText(this@HomeActivity, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    // arrumar para direcionar para outra tela
                } else {
                    Toast.makeText(this@HomeActivity, "Erro no login. Verifique as credenciais.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Falha na conexão: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("HomeActivity", "Erro: ${t.message}")
            }
        })
    }

}