package com.example.ddm_vetspace.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val user_id: Int = 0,
    val nome: String,
    val email: String,
    val telefone: String? = null,
    val senha: String,
    val permissao: Int? = null
)
