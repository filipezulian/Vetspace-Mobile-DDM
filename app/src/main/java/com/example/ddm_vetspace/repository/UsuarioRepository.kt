package com.example.ddm_vetspace.repository

import com.example.ddm_vetspace.dto.CadastroRequest
import com.example.ddm_vetspace.interfaces.usuarioService
import com.example.ddm_vetspace.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UsuarioRepository(
    private val apiService: usuarioService
) {
    suspend fun cadastrar(nome: String, email: String, telefone: String, senha: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            // Hash da senha usando SHA-256
            val senhaHashed = hashSenha(senha)

            // Cria o request de cadastro
            val cadastroRequest = CadastroRequest(nome, email, telefone, senhaHashed)

            return@withContext try {
                // Tenta cadastrar via API
                val response = apiService.cadastrar(cadastroRequest).execute()
                if (response.isSuccessful) {
                    Result.success(Unit) // Retorna sucesso sem corpo
                } else {
                    Result.failure(Exception("Erro no cadastro via API: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun hashSenha(senha: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(senha.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
