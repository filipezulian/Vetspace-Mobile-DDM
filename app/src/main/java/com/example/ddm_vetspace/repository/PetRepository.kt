package com.example.ddm_vetspace.repository

import com.example.ddm_vetspace.interfaces.petService
import com.example.ddm_vetspace.model.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class PetRepository (private val petApi: petService) {

    suspend fun cadastrarPet(usuarioId: Long, pet: Pet): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<Void> = petApi.cadastrarPet(usuarioId, pet).execute()
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Erro ao cadastrar pet: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}