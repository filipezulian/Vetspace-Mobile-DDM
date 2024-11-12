package com.example.ddm_vetspace.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.impl.utils.futures.SettableFuture
import com.example.ddm_vetspace.model.Blog
import com.example.ddm_vetspace.repository.BlogRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer
import com.example.ddm_vetspace.services.blogService
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class background_service_blog(appContext: Context,
                              workerParams: WorkerParameters
) : ListenableWorker(appContext, workerParams) {

    private val blogPgServices: blogService = RetrofitInitializer.blogApi
    private val blogRepository: BlogRepository

    init {
        val dbHelper = DatabaseHelper(appContext)
        blogRepository = BlogRepository(dbHelper)
    }

    override fun startWork(): ListenableFuture<Result> {
        val future = SettableFuture.create<Result>()

        CoroutineScope(Dispatchers.IO).launch {
            syncBlogDb()
            future.set(Result.success())
        }

        return future
    }

    private suspend fun syncBlogDb() {
        val postgresBlogs: List<Blog> = getBlogsPostgres()
        val sqliteBlogs = getBlogsSQLite()

        val results = bruteForceCompareBlog(postgresBlogs, sqliteBlogs, idSelector = { it.id })
        val missingInSQLite = results["missingInSQLite"] as List<Blog>
        if (missingInSQLite.isNotEmpty()) {
            insertBlogIntoSQLite(missingInSQLite)
        }

    }

    private fun <T> bruteForceCompareBlog(postgres: List<T>, sqlite: List<T>, idSelector: (T) -> Int?): Map<String, List<T>> {
        val missingInSQLite = postgres.filter { pgEntity ->
            sqlite.none { sqliteEntity -> idSelector(sqliteEntity) == idSelector(pgEntity) }
        }

        return mapOf(
            "missingInSQLite" to missingInSQLite
        )
    }

    private suspend fun getBlogsSQLite(): List<Blog> {
        return try {
            blogRepository.getBlogs().getOrElse { emptyList() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun getBlogsPostgres(): List<Blog> {
        return try {
            val response = withContext(Dispatchers.IO) { blogPgServices.getBlogs().execute() }
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun insertBlogIntoSQLite(blogs: List<Blog>) {
        try {
            blogs.forEach { blog ->
                withContext(Dispatchers.IO) {
                    blogRepository.inserirBlog(blog)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
