package com.example.ddm_vetspace

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class adicionarPet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_pet)

        val petNames = listOf("Pandora", "Lilo", "Beta", "Charlie", "Delta")

        val recyclerViewPets = findViewById<RecyclerView>(R.id.recyclerViewPets)
        recyclerViewPets.layoutManager = LinearLayoutManager(this)
        val adapter = PetAdapter(petNames)
        recyclerViewPets.adapter = adapter

        val addButton = findViewById<Button>(R.id.buttonAddPet)
        addButton.setOnClickListener {
            val intent = Intent(this, AddPet::class.java)
            startActivity(intent)
        }

        val blogsButton = findViewById<ImageView>(R.id.buttonMenu)
        blogsButton.setOnClickListener {
            val intent = Intent(this, Blogs::class.java)
            startActivity(intent)
        }

    }
}

class PetAdapter(private val petNames: List<String>) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val petName = petNames[position]
        holder.textViewPetName.text = petName
    }

    override fun getItemCount(): Int {
        return petNames.size
    }

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPetName: TextView = itemView.findViewById(R.id.textViewPetName)
    }
}
