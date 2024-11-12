package com.example.ddm_vetspace.database

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.impl.utils.futures.SettableFuture
import com.example.ddm_vetspace.model.Pet
import com.example.ddm_vetspace.repository.PetRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer
import com.example.ddm_vetspace.services.petService
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class background_service_pet(appContext: Context,
                           workerParams: WorkerParameters
) : ListenableWorker(appContext, workerParams) {

    private val petService: petService = RetrofitInitializer.petApi
    private val petRepository: PetRepository
    private val userId: Int

    init {
        val dbHelper = DatabaseHelper(appContext)
        petRepository = PetRepository(petService, dbHelper)
        val sharedPreferences = appContext.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("user_id", -1)
    }

    override fun startWork(): ListenableFuture<Result> {
        val future = SettableFuture.create<Result>()

        CoroutineScope(Dispatchers.IO).launch {
            if (userId != -1) {
                syncPetDb()
            }
            future.set(Result.success())
        }

        return future
    }

    private suspend fun syncPetDb() {
        val postgresPets: List<Pet> = getPetsPostgres()
        val sqlitePets = getPetsSQLite()

        val results = bruteForceCompare(postgresPets, sqlitePets, idSelector = { it.pet_id })
        val missingInSQLite = results["missingInSQLite"] as List<Pet>

        Log.d("ComparisonResults", "Comparison results: $missingInSQLite")
        if (missingInSQLite.isNotEmpty()) {
            insertPetIntoSQLite(missingInSQLite)
        }
    }

    private fun <T> bruteForceCompare(postgres: List<T>, sqlite: List<T>, idSelector: (T) -> Int?): Map<String, List<T>> {
        val missingInSQLite = postgres.filter { pgEntity ->
            sqlite.none { sqliteEntity -> idSelector(sqliteEntity) == idSelector(pgEntity) }
        }

        return mapOf(
            "missingInSQLite" to missingInSQLite
        )
    }

    private suspend fun getPetsSQLite(): List<Pet> {
        return try {
            petRepository.buscarPetsPorUsuario(userId).getOrElse { emptyList() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun getPetsPostgres(): List<Pet> {
        return try {
            val response = withContext(Dispatchers.IO) { petService.buscarPetsPorUsuarioId(userId.toLong()).execute() }
            if (response.isSuccessful) {
                response.body()?.map { it.toPet() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun insertPetIntoSQLite(pets: List<Pet>) {
        try {
            pets.forEach { pet ->
                withContext(Dispatchers.IO) {
                    petRepository.inserirPet(pet, userId)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}