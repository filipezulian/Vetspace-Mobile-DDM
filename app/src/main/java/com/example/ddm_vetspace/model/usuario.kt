package com.example.ddm_vetspace.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("id")
    val user_id: Int = 0,
    val nome: String,
    val email: String,
    val telefone: String? = null,
    val senha: String,
    val permissao: Int? = null
)
