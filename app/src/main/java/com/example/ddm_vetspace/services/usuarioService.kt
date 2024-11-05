package com.example.ddm_vetspace.repository

import com.example.ddm_vetspace.dto.LoginRequest
import com.example.ddm_vetspace.model.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface usuarioService {
    @POST("usuario/autenticar")
    fun autenticar(@Body loginRequest: LoginRequest): Call<Usuario>
}