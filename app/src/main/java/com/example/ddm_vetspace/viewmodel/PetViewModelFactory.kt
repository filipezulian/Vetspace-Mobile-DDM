package com.example.ddm_vetspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ddm_vetspace.repository.PetRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer

class PetViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val petRepository = PetRepository(RetrofitInitializer.petApi)
        return PetViewModel(petRepository) as T
    }
}