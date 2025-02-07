package edu.alvarocervantes.myfavoritepet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val petList = mutableListOf<Pet>()
    private lateinit var petAdapter: PetAdapter

    private val addEditPetLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newPet = result.data?.getSerializableExtra("NEW_PET") as? Pet
            newPet?.let { pet ->
                val existingPetIndex = petList.indexOfFirst { it.id == pet.id }
                if (existingPetIndex != -1) {
                    petList[existingPetIndex] = pet
                    petAdapter.notifyItemChanged(existingPetIndex)
                } else {
                    petList.add(pet)
                    petAdapter.notifyItemInserted(petList.size - 1)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val rvPets = findViewById<RecyclerView>(R.id.rvPets)
        val fabAddPet = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddPet)

        // Configurar RecyclerView
        petAdapter = PetAdapter(petList, { pet ->
            val intent = Intent(this, AddEditPetActivity::class.java)
            intent.putExtra("EDIT_PET", pet)
            addEditPetLauncher.launch(intent)
        }, { pet ->
            removePet(pet)
        }, { pet ->
            toggleFavorite(pet)
        })

        rvPets.layoutManager = LinearLayoutManager(this)
        rvPets.adapter = petAdapter

        fabAddPet.setOnClickListener {
            val intent = Intent(this, AddEditPetActivity::class.java)
            addEditPetLauncher.launch(intent)
        }
    }

    private fun removePet(pet: Pet) {
        val position = petList.indexOf(pet)
        if (position != -1) {
            petList.removeAt(position)
            petAdapter.notifyItemRemoved(position)
        }
    }

    private fun toggleFavorite(pet: Pet) {
        val index = petList.indexOfFirst { it.id == pet.id }
        if (index != -1) {
            petList[index].isFavorite = !petList[index].isFavorite
            petAdapter.notifyItemChanged(index)
        }
    }
}
