package com.example.ddm_vetspace.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey(autoGenerate = true) val pet_id: Int = 0,
    val tipo: Int? = null,
    val sexo: Boolean? = null,
    val nome: String,
    val nascimento: String? = null,
    val user_id: Int? = null
)
