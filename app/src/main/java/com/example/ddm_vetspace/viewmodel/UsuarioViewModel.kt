package com.example.ddm_vetspace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ddm_vetspace.model.Usuario
import com.example.ddm_vetspace.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<Usuario>>()
    val loginResult: LiveData<Result<Usuario>> get() = _loginResult

    private val _registerResult = MutableLiveData<Result<Unit>>()
    val registerResult: LiveData<Result<Unit>> get() = _registerResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun login(email: String, senha: String) {

        viewModelScope.launch {
            try {
                val result = repository.login(email, senha)
                _loginResult.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Erro ao realizar login"
            }
        }
    }

    fun register(nome: String, email: String, telefone: String, senha: String) {
        viewModelScope.launch {
            val result = repository.cadastrar(nome, email, telefone, senha)
            _registerResult.value = result
        }
    }
}
