package com.example.ddm_vetspace.repository

import retrofit2.Call
import com.example.ddm_vetspace.model.Blog
import retrofit2.http.GET

interface blogService {
    @GET("blog")
    fun getAllBlogs(): Call<List<Blog>>
}