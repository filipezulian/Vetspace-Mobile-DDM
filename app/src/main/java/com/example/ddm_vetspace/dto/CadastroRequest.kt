package com.example.ddm_vetspace.dto

data class CadastroRequest(
    val nome: String,
    val email: String,
    val telefone: String,
    val senha: String
)