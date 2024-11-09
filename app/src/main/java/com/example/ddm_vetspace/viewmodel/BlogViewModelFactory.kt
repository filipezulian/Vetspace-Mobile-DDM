package com.example.ddm_vetspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ddm_vetspace.database.DatabaseHelper
import com.example.ddm_vetspace.repository.BlogRepository

class BlogViewModelFactory(private val dbHelper: DatabaseHelper) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
            //garantir que modelClass é do tipo BlogViewModel.
            val blogRepository = BlogRepository(dbHelper)
            BlogViewModel(blogRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}