package edu.alvarocervantes.myfavoritepet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditPetActivity : AppCompatActivity() {
    private var selectedImageUri: Uri? = null
    private var petId: Int? = null // ID de la mascota si se está editando

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_pet)

        val etName = findViewById<EditText>(R.id.etName)
        val etScientificName = findViewById<EditText>(R.id.etScientificName)
        val etFurType = findViewById<EditText>(R.id.etFurType)
        val etCategory = findViewById<EditText>(R.id.etCategory)
        val etWikiLink = findViewById<EditText>(R.id.etWikiLink)
        val rbLoveLevel = findViewById<RatingBar>(R.id.rbLoveLevel)
        val btnAddImage = findViewById<Button>(R.id.btnAddImage)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Verificar si estamos en modo edición
        val pet = intent.getSerializableExtra("EDIT_PET") as? Pet
        pet?.let {
            petId = it.id
            etName.setText(it.name)
            etScientificName.setText(it.scientificName)
            etFurType.setText(it.furType)
            etCategory.setText(it.category)
            etWikiLink.setText(it.wikiLink)
            rbLoveLevel.rating = it.loveLevel.toFloat()
            selectedImageUri = it.imageUri?.let { uri -> Uri.parse(uri) }
        }

        btnAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        btnSave.setOnClickListener {
            // Validar que los campos no estén vacíos
            if (etName.text.isEmpty() || etCategory.text.isEmpty()) {
                Toast.makeText(this, "Nombre y categoría son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPet = Pet(
                id = petId ?: (0..1000000).random(),
                name = etName.text.toString(),
                scientificName = etScientificName.text.toString(),
                furType = etFurType.text.toString(),
                category = etCategory.text.toString(),
                imageUri = selectedImageUri?.toString(),
                loveLevel = rbLoveLevel.rating.toInt(),
                wikiLink = etWikiLink.text.toString()
            )

            val resultIntent = Intent().apply {
                putExtra("NEW_PET", newPet)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
        }
    }
}
