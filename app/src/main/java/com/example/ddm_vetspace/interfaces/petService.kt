package com.example.ddm_vetspace.interfaces

import com.example.ddm_vetspace.dto.PetResponse
import com.example.ddm_vetspace.model.Pet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface petService {
    @GET("/pet/usuario/{usuarioId}/pets")
    fun buscarPetsPorUsuarioId(@Path("usuarioId") usuarioId: Long): Call<List<PetResponse>>

    @POST("/pet/cadastrar/{usuarioId}")
    fun cadastrarPet(@Path("usuarioId") usuarioId: Long, @Body pet: Pet): Call<Void>
}