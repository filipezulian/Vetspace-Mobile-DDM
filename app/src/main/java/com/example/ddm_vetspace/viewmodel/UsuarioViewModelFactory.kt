package com.example.ddm_vetspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ddm_vetspace.repository.UsuarioRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer

class UsuarioViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val usuarioRepository = UsuarioRepository(RetrofitInitializer.usuarioApi)
        return UsuarioViewModel(usuarioRepository) as T
    }
}
