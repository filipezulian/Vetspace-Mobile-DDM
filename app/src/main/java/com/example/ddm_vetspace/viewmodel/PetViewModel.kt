package com.example.ddm_vetspace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ddm_vetspace.dto.PetResponse
import com.example.ddm_vetspace.model.Pet
import com.example.ddm_vetspace.repository.PetRepository
import kotlinx.coroutines.launch

class PetViewModel(private val repository: PetRepository) : ViewModel() {

    private val _pets = MutableLiveData<List<PetResponse>>()
    val pets: LiveData<List<PetResponse>> get() = _pets

    private val _addPetStatus = MutableLiveData<Result<Unit>>()
    val addPetStatus: LiveData<Result<Unit>> get() = _addPetStatus

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadPetsByUserId(userId: Long) {
        viewModelScope.launch {
            val result = repository.buscarPetsPorUsuarioId(userId)
            result.onSuccess {
                _pets.value = it
            }.onFailure {
                _error.value = it.message ?: "Erro ao carregar pets"
            }
        }
    }

    fun addPet(userId: Long, pet: Pet) {
        viewModelScope.launch {
            val result = repository.cadastrarPet(userId, pet)
            _addPetStatus.value = result
        }
    }
}
