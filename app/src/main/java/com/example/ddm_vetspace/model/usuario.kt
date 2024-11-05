package com.example.ddm_vetspace.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val user_id: Int = 0,
    val nome: String,
    val email: String,
    val telefone: String? = null,
    val senha: String,
    val permissao: Int? = null
)
