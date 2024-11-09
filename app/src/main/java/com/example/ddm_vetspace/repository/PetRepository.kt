package com.example.ddm_vetspace.repository

import com.example.ddm_vetspace.database.DatabaseHelper
import com.example.ddm_vetspace.dto.PetResponse
import com.example.ddm_vetspace.services.petService
import com.example.ddm_vetspace.model.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class PetRepository (private val petApi: petService, private val dbHelper: DatabaseHelper) {

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

//    suspend fun buscarPetsPorUsuarioId(usuarioId: Long): Result<List<PetResponse>> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = petApi.buscarPetsPorUsuarioId(usuarioId).execute()
//                if (response.isSuccessful) {
//                    val petResponses = response.body() ?: emptyList()
//                    Result.success(petResponses)
//                } else {
//                    Result.failure(Exception("Erro ao buscar pets: ${response.code()}"))
//                }
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//    }

    // Método para buscar pets no SQLite
    suspend fun buscarPetsPorUsuario(userId: Int): Result<List<Pet>> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                "pet",
                arrayOf("pet_id", "tipo", "sexo", "nome", "nascimento", "user_id"),
                "user_id = ?",
                arrayOf(userId.toString()),
                null, null, null
            )

            val pets = mutableListOf<Pet>()
            while (cursor.moveToNext()) {
                val pet = Pet(
                    pet_id = cursor.getInt(cursor.getColumnIndexOrThrow("pet_id")),
                    tipo = cursor.getInt(cursor.getColumnIndexOrThrow("tipo")),
                    sexo = cursor.getInt(cursor.getColumnIndexOrThrow("sexo")) == 1,
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    nascimento = cursor.getString(cursor.getColumnIndexOrThrow("nascimento")),
                    user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
                )
                pets.add(pet)
            }
            cursor.close()
            db.close()

            Result.success(pets)
        }
    }
}