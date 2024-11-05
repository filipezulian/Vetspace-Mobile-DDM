package com.example.ddm_vetspace.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val pet_id: Int = 0,
    val tipo: Int? = null,
    val sexo: Boolean? = null,
    val nome: String,
    val nascimento: String? = null,
    val user_id: Int? = null
)
