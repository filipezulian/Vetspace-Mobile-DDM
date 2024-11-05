package com.example.ddm_vetspace.interfaces

import com.example.ddm_vetspace.model.Blog
import retrofit2.Call
import retrofit2.http.GET

interface blogService {
    @GET("blog")
    fun getBlogs(): Call<List<Blog>>
}