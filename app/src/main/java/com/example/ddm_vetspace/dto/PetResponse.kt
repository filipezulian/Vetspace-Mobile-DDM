package com.example.ddm_vetspace.dto

import com.example.ddm_vetspace.model.Pet
import com.google.gson.annotations.SerializedName

data class PetResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("sexo")
    val sexo: Boolean,

    @SerializedName("nome")
    val nome: String,

    @SerializedName("nascimento")
    val nascimento: String // LocalDate vindo como String no formato yyyy-MM-dd
) { fun toPet(): Pet {
    return Pet(
        pet_id = this.id.toInt(),
        tipo = this.tipo,
        sexo = this.sexo,
        nome = this.nome,
        nascimento = this.nascimento,
        user_id = null
    )
}
}