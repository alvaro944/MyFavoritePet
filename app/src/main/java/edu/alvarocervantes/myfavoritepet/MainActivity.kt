package edu.alvarocervantes.myfavoritepet

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var petAdapter: PetAdapter
    private val petList = mutableListOf<Pet>()

    private val addPetLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val newPet = result.data?.getSerializableExtra("NEW_PET") as? Pet
                newPet?.let {
                    petList.add(it)
                    petAdapter.notifyDataSetChanged()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPets)
        recyclerView.layoutManager = LinearLayoutManager(this)
        petAdapter = PetAdapter(petList)
        recyclerView.adapter = petAdapter

        // Configurar botón flotante
        val fabAddPet: FloatingActionButton = findViewById(R.id.fabAddPet)
        fabAddPet.setOnClickListener {
            val intent = Intent(this, AddEditPetActivity::class.java)
            addPetLauncher.launch(intent)
        }
    }
}
