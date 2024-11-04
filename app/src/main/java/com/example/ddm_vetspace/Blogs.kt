package com.example.ddm_vetspace

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Blog(val title: String, val description: String, val date: String)

class Blogs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blogs)

        val blogList = listOf(
            Blog("Feliz Halloween!", "Feliz Halloween para todos os nossos pets! Não dê chocolate pros pequenos! É perigoso para eles!", "31/10"),
            Blog("Ullamcorper!", "Lorem ipsum dolor sit amet consectetur. Vulputate faucibus quisque eget nec", "31/03")
        )

        val recyclerViewBlogs = findViewById<RecyclerView>(R.id.recyclerViewBlogs)
        recyclerViewBlogs.layoutManager = LinearLayoutManager(this)
        val adapter = BlogAdapter(blogList)
        recyclerViewBlogs.adapter = adapter

        val petsButton = findViewById<ImageView>(R.id.buttonPets)
        petsButton.setOnClickListener {
            val intent = Intent(this, adicionarPet::class.java)
            startActivity(intent)
        }
    }
}

class BlogAdapter(private val blogList: List<Blog>) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog, parent, false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogList[position]
        holder.textViewBlogTitle.text = blog.title
        holder.textViewBlogDescription.text = blog.description
        holder.textViewBlogDate.text = blog.date
    }

    override fun getItemCount(): Int = blogList.size

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBlogTitle: TextView = itemView.findViewById(R.id.textViewBlogTitle)
        val textViewBlogDescription: TextView = itemView.findViewById(R.id.textViewBlogDescription)
        val textViewBlogDate: TextView = itemView.findViewById(R.id.textViewBlogDate)
    }
}