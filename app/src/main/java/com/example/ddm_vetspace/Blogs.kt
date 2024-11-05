package com.example.ddm_vetspace

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddm_vetspace.model.Blog
import com.example.ddm_vetspace.repository.BlogRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer
import kotlinx.coroutines.launch

class Blogs : AppCompatActivity() {

    private lateinit var repository: BlogRepository
    private lateinit var blogAdapter: BlogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blogs)

        // Inicializando o repositório com o Retrofit
        repository = BlogRepository(RetrofitInitializer.blogApi)

        val recyclerViewBlogs = findViewById<RecyclerView>(R.id.recyclerViewBlogs)
        recyclerViewBlogs.layoutManager = LinearLayoutManager(this)
        blogAdapter = BlogAdapter(emptyList())
        recyclerViewBlogs.adapter = blogAdapter

        val petsButton = findViewById<ImageView>(R.id.buttonPets)
        petsButton.setOnClickListener {
            val intent = Intent(this, adicionarPet::class.java)
            startActivity(intent)
        }
        // Carregar blogs da API
        loadBlogs()
    }

    private fun loadBlogs() {
        lifecycleScope.launch {
            val result = repository.getBlogs()
            result.onSuccess { blogs ->
                blogAdapter.updateData(blogs)
            }.onFailure {
                Toast.makeText(this@Blogs, "Erro ao carregar blogs: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

class BlogAdapter(private var blogList: List<Blog>) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog, parent, false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogList[position]
        holder.textViewBlogTitle.text = blog.titulo
        holder.textViewBlogDescription.text = blog.descricao
        holder.textViewBlogDate.text = blog.data
    }

    override fun getItemCount(): Int = blogList.size

    fun updateData(newBlogList: List<Blog>) {
        blogList = newBlogList
    }

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBlogTitle: TextView = itemView.findViewById(R.id.textViewBlogTitle)
        val textViewBlogDescription: TextView = itemView.findViewById(R.id.textViewBlogDescription)
        val textViewBlogDate: TextView = itemView.findViewById(R.id.textViewBlogDate)
    }
}