package com.example.ddm_vetspace.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddm_vetspace.R
import com.example.ddm_vetspace.model.Blog
import com.example.ddm_vetspace.repository.BlogRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer
import com.example.ddm_vetspace.viewmodel.BlogViewModel
import com.example.ddm_vetspace.viewmodel.BlogViewModelFactory

class Blogs : AppCompatActivity() {

    private val blogViewModel: BlogViewModel by viewModels {
        BlogViewModelFactory(BlogRepository(RetrofitInitializer.blogApi))
    }
    private lateinit var blogAdapter: BlogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blogs)

        val recyclerViewBlogs = findViewById<RecyclerView>(R.id.recyclerViewBlogs)
        recyclerViewBlogs.layoutManager = LinearLayoutManager(this)
        blogAdapter = BlogAdapter(emptyList())
        recyclerViewBlogs.adapter = blogAdapter

        val petsButton = findViewById<ImageView>(R.id.buttonPets)
        petsButton.setOnClickListener {
            val intent = Intent(this, adicionarPet::class.java)
            startActivity(intent)
        }

        // Observe data from ViewModel
        blogViewModel.blogs.observe(this, Observer { blogs ->
            blogAdapter.updateData(blogs)
        })

        blogViewModel.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })

        // Load blogs via ViewModel
        blogViewModel.loadBlogs()
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
        notifyDataSetChanged()
    }

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBlogTitle: TextView = itemView.findViewById(R.id.textViewBlogTitle)
        val textViewBlogDescription: TextView = itemView.findViewById(R.id.textViewBlogDescription)
        val textViewBlogDate: TextView = itemView.findViewById(R.id.textViewBlogDate)
    }
}