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
    private val allPets = mutableListOf<Pet>() // Lista completa sin filtrar
    private lateinit var petAdapter: PetAdapter
    private var showOnlyFavorites = false // Estado del filtro

    private val addEditPetLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newPet = result.data?.getSerializableExtra("NEW_PET") as? Pet
            newPet?.let { pet ->
                val existingPetIndex = allPets.indexOfFirst { it.id == pet.id }
                if (existingPetIndex != -1) {
                    allPets[existingPetIndex] = pet
                } else {
                    allPets.add(pet)
                }
                applyFilter()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Establecer Toolbar como ActionBar
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

        // Botón flotante para añadir mascota
        fabAddPet.setOnClickListener {
            val intent = Intent(this, AddEditPetActivity::class.java)
            addEditPetLauncher.launch(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                allPets.sortBy { it.name }
                applyFilter()
                true
            }
            R.id.action_sort_by_love_level -> {
                allPets.sortByDescending { it.loveLevel }
                applyFilter()
                true
            }
            R.id.action_show_favorites -> {
                showOnlyFavorites = !showOnlyFavorites
                item.isChecked = showOnlyFavorites
                applyFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun applyFilter() {
        petList.clear()
        if (showOnlyFavorites) {
            petList.addAll(allPets.filter { it.isFavorite })
        } else {
            petList.addAll(allPets)
        }
        petAdapter.notifyDataSetChanged()
    }

    private fun removePet(pet: Pet) {
        allPets.remove(pet)
        applyFilter()
    }

    private fun toggleFavorite(pet: Pet) {
        val index = allPets.indexOfFirst { it.id == pet.id }
        if (index != -1) {
            allPets[index].isFavorite = !allPets[index].isFavorite
            applyFilter()
        }
    }
}
