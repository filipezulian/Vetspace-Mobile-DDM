package com.example.ddm_vetspace.repository

import com.example.ddm_vetspace.interfaces.blogService
import com.example.ddm_vetspace.model.Blog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlogRepository (
    private val blogService: blogService
) {
    suspend fun getBlogs(): Result<List<Blog>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = blogService.getBlogs().execute()
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Erro ao buscar blogs: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}