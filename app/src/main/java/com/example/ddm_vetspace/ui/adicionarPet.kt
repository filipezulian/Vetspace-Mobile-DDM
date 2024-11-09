package com.example.ddm_vetspace.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddm_vetspace.R
import com.example.ddm_vetspace.database.App
import com.example.ddm_vetspace.dto.PetResponse
import com.example.ddm_vetspace.model.Pet
import com.example.ddm_vetspace.viewmodel.PetViewModel
import com.example.ddm_vetspace.viewmodel.PetViewModelFactory

class adicionarPet : AppCompatActivity() {

    private lateinit var petAdapter: PetAdapter
    private lateinit var addPetLauncher: ActivityResultLauncher<Intent>

    // Inicializa o PetViewModel com o DatabaseHelper
    private val petViewModel: PetViewModel by viewModels {
        val databaseHelper = (application as App).databaseHelper
        PetViewModelFactory(databaseHelper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_pet)

        val recyclerViewPets = findViewById<RecyclerView>(R.id.recyclerViewPets)
        recyclerViewPets.layoutManager = LinearLayoutManager(this)
        petAdapter = PetAdapter(emptyList())
        recyclerViewPets.adapter = petAdapter

        petViewModel.pets.observe(this) { pets ->
            petAdapter.updatePets(pets)
        }

        petViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        if (userId != -1) {
            petViewModel.loadPetsByUserId(userId)
        } else {
            Toast.makeText(this, "Usuário não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show()
        }

        val addButton = findViewById<ImageView>(R.id.buttonAddPet)
        addButton.setOnClickListener {
            val intent = Intent(this, AddPet::class.java)
            addPetLauncher.launch(intent)
        }

        val blogsButton = findViewById<ImageView>(R.id.buttonMenu)
        blogsButton.setOnClickListener {
            val intent = Intent(this, Blogs::class.java)
            addPetLauncher.launch(intent)
        }

        addPetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                petViewModel.loadPetsByUserId(userId)
            }
        }
    }
}



class PetAdapter(private var pets: List<Pet>) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.textViewPetName.text = pet.nome
    }

    override fun getItemCount(): Int = pets.size

    fun updatePets(newPets: List<Pet>) {
        pets = newPets
        notifyDataSetChanged()
    }

//    fun addPet(pet: Pet) {
//        pets = pets + pet
//        notifyDataSetChanged()
//    }

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPetName: TextView = itemView.findViewById(R.id.textViewPetName)
    }
}
