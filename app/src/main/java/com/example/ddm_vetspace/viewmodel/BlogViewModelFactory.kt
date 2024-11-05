package com.example.ddm_vetspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ddm_vetspace.repository.BlogRepository

class BlogViewModelFactory(private val blogRepository: BlogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlogViewModel(blogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}