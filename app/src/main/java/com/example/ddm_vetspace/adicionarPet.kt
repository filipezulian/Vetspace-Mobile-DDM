package com.example.ddm_vetspace

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ddm_vetspace.dto.PetResponse
import com.example.ddm_vetspace.repository.PetRepository
import com.example.ddm_vetspace.retrofit.RetrofitInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class adicionarPet : AppCompatActivity() {

    private lateinit var petAdapter: PetAdapter
    private lateinit var petRepository: PetRepository
    private lateinit var addPetLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_pet)

        petRepository = PetRepository(RetrofitInitializer.petApi)

        val recyclerViewPets = findViewById<RecyclerView>(R.id.recyclerViewPets)
        recyclerViewPets.layoutManager = LinearLayoutManager(this)
        petAdapter = PetAdapter(emptyList())
        recyclerViewPets.adapter = petAdapter

        // Inicializa o launcher para a Activity AddPet
        addPetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Recarrega a lista de pets após adicionar um novo pet
                val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("user_id", -1)
                if (userId != -1) {
                    loadPets(userId.toLong())
                }
            }
        }

        val addButton = findViewById<Button>(R.id.buttonAddPet)
        addButton.setOnClickListener {
            val intent = Intent(this, AddPet::class.java)
            startActivity(intent)
            addPetLauncher.launch(intent)
        }

        val blogsButton = findViewById<ImageView>(R.id.buttonMenu)
        blogsButton.setOnClickListener {
            val intent = Intent(this, Blogs::class.java)
            startActivity(intent)
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId != -1) {
            loadPets(userId.toLong())
        } else {
            Toast.makeText(this, "Usuário não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPets(userId: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) { petRepository.buscarPetsPorUsuarioId(userId) }
            result.onSuccess { pets ->
                if (pets.isNotEmpty()) {
                    Log.d("adicionarPet", "Pets carregados: ${pets.size}")
                    petAdapter.updatePets(pets)
                } else {
                    Log.d("adicionarPet", "Nenhum pet encontrado para o usuário.")
                    Toast.makeText(this@adicionarPet, "Nenhum pet encontrado.", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { error ->
                Log.e("adicionarPet", "Erro ao carregar pets: ${error.message}")
                Toast.makeText(this@adicionarPet, "Erro ao carregar pets", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



class PetAdapter(private var pets: List<PetResponse>) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.textViewPetName.text = pet.nome
    }

    override fun getItemCount(): Int = pets.size

    fun updatePets(newPets: List<PetResponse>) {
        pets = newPets
        notifyDataSetChanged()
    }

    fun addPet(pet: PetResponse) {
        pets = pets + pet // Adiciona o novo pet à lista existente
        notifyDataSetChanged()
    }

    class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPetName: TextView = itemView.findViewById(R.id.textViewPetName)
    }
}
