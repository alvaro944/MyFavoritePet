package edu.alvarocervantes.myfavoritepet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.alvarocervantes.myfavoritepet.adapters.PetAdapter
import edu.alvarocervantes.myfavoritepet.data.PetDatabaseHelper
import edu.alvarocervantes.myfavoritepet.model.Pet
import edu.alvarocervantes.myfavoritepet.utils.FirebaseBackupHelper

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: PetDatabaseHelper
    private val originalPetList = mutableListOf<Pet>() // Lista completa sin modificaciones
    private val petList = mutableListOf<Pet>() // Lista visible en la UI
    private lateinit var petAdapter: PetAdapter

    private val addEditPetLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newPet = result.data?.getSerializableExtra("NEW_PET") as? Pet
            newPet?.let { pet ->
                val existingPetIndex = petList.indexOfFirst { it.id == pet.id }
                if (existingPetIndex != -1) {
                    // Actualizar en ambas listas y en SQLite
                    petList[existingPetIndex] = pet
                    originalPetList[existingPetIndex] = pet
                    dbHelper.updatePet(pet) // Guardar cambios en la BD
                    petAdapter.notifyItemChanged(existingPetIndex)
                } else {
                    // Agregar nueva mascota en SQLite
                    val newId = dbHelper.insertPet(pet)
                    val petWithId = pet.copy(id = newId.toInt()) // SQLite asigna ID automático
                    petList.add(petWithId)
                    originalPetList.add(petWithId)
                    petAdapter.notifyItemInserted(petList.size - 1)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = PetDatabaseHelper(this) // Inicializar la BD

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val rvPets = findViewById<RecyclerView>(R.id.rvPets)
        val fabAddPet = findViewById<FloatingActionButton>(R.id.fabAddPet)

        // Cargar mascotas desde SQLite
        originalPetList.addAll(dbHelper.getAllPets())
        petList.addAll(originalPetList)

        petAdapter = PetAdapter(petList, { pet ->
            val intent = Intent(this, AddEditPetActivity::class.java)
            intent.putExtra("EDIT_PET", pet) // Pasar la mascota seleccionada
            addEditPetLauncher.launch(intent)
        }, { pet ->
            removePet(pet)
        }, { pet ->
            toggleFavorite(pet)
        }, { wikiLink -> // 🔥 Nuevo parámetro para manejar el botón de Info
            if (wikiLink.isNotEmpty()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiLink))
                startActivity(browserIntent)
            } else {
                Toast.makeText(this, "No hay enlace disponible", Toast.LENGTH_SHORT).show()
            }
        })

        rvPets.layoutManager = LinearLayoutManager(this)
        rvPets.adapter = petAdapter

        fabAddPet.setOnClickListener {
            val intent = Intent(this, AddEditPetActivity::class.java)
            addEditPetLauncher.launch(intent)
        }
    }

    // Método para crear el menú en la Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Manejo de opciones del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_name -> {
                petList.sortBy { it.name }
                petAdapter.notifyDataSetChanged()
            }
            R.id.action_sort_name_desc -> {
                petList.sortByDescending { it.name }
                petAdapter.notifyDataSetChanged()
            }
            R.id.action_sort_love -> {
                petList.sortByDescending { it.loveLevel }
                petAdapter.notifyDataSetChanged()
            }
            R.id.action_filter_favorites -> {
                val filteredList = originalPetList.filter { it.isFavorite }
                petList.clear()
                petList.addAll(filteredList)
                petAdapter.notifyDataSetChanged()
            }
            R.id.action_show_all -> {
                petList.clear()
                petList.addAll(originalPetList) // Restaurar desde la lista original
                petAdapter.notifyDataSetChanged()
            }
            R.id.action_backup_firebase -> {
                FirebaseBackupHelper.backupPets(originalPetList,
                    onSuccess = {
                        Toast.makeText(this, "Backup exitoso en Firebase", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { e ->
                        Toast.makeText(this, "Error en backup: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
            R.id.action_restore_firebase -> {
                FirebaseBackupHelper.getLatestBackupCollection(
                    onSuccess = { latestCollection ->
                        FirebaseBackupHelper.restorePets(latestCollection,
                            onSuccess = { pets ->
                                var added = 0
                                pets.forEach { pet ->
                                    if (!originalPetList.any { it.id == pet.id }) { // Evitar duplicados
                                        dbHelper.insertPet(pet) // 🔥 Guardamos en SQLite
                                        originalPetList.add(pet) // 🔥 Actualizamos lista original
                                        petList.add(pet) // 🔥 Mostramos en UI
                                        added++
                                    }
                                }
                                petAdapter.notifyDataSetChanged() // 🔥 Refrescar UI
                                if (added > 0) {
                                    Toast.makeText(this, "Restauración completada: $added elementos añadidos.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "No se añadieron elementos (posible duplicado).", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onFailure = { e ->
                                Toast.makeText(this, "Error al restaurar: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    onFailure = { e ->
                        Toast.makeText(this, "No se encontró un backup en Firebase.", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removePet(pet: Pet) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Eliminar mascota")
        builder.setMessage("¿Seguro que quieres eliminar a ${pet.name}? Esta acción no se puede deshacer.")

        builder.setPositiveButton("Sí") { _, _ ->
            val position = petList.indexOf(pet)
            if (position != -1) {
                dbHelper.deletePet(pet.id) // Eliminar de SQLite
                petList.removeAt(position)
                originalPetList.remove(pet)
                petAdapter.notifyItemRemoved(position)
                Toast.makeText(this, "${pet.name} eliminada", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun toggleFavorite(pet: Pet) {
        val index = originalPetList.indexOfFirst { it.id == pet.id }
        if (index != -1) {
            originalPetList[index].isFavorite = !originalPetList[index].isFavorite
            val visibleIndex = petList.indexOfFirst { it.id == pet.id }
            if (visibleIndex != -1) {
                petList[visibleIndex].isFavorite = originalPetList[index].isFavorite
                petAdapter.notifyItemChanged(visibleIndex)
            }
            dbHelper.updatePet(originalPetList[index])
        }
    }
}
