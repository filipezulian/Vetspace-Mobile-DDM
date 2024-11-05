package com.example.ddm_vetspace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ddm_vetspace.model.Blog
import com.example.ddm_vetspace.repository.BlogRepository
import kotlinx.coroutines.launch

class BlogViewModel(private val blogRepository: BlogRepository) : ViewModel() {

    private val _blogs = MutableLiveData<List<Blog>>()
    val blogs: LiveData<List<Blog>> get() = _blogs

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadBlogs() {
        viewModelScope.launch {
            val result = blogRepository.getBlogs()
            result.onSuccess { blogList ->
                _blogs.value = blogList
            }.onFailure { exception ->
                _error.value = "Erro ao carregar blogs: ${exception.message}"
            }
        }
    }
}
