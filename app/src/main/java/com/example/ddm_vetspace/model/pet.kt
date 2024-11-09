package com.example.ddm_vetspace.model

import com.google.gson.annotations.SerializedName

data class Pet(
    @SerializedName("id")
    val pet_id: Int = 0,
    val tipo: Int? = null,
    val sexo: Boolean? = null,
    val nome: String,
    val nascimento: String? = null,
    val user_id: Int? = null
)
