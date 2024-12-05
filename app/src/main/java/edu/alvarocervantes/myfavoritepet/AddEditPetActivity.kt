package edu.alvarocervantes.myfavoritepet

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity

class AddEditPetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_pet)

        val editName: EditText = findViewById(R.id.editTextName)
        val editScientificName: EditText = findViewById(R.id.editTextScientificName)
        val editFurType: EditText = findViewById(R.id.editTextFurType)
        val editClassification: EditText = findViewById(R.id.editTextClassification)
        val ratingBar: RatingBar = findViewById(R.id.ratingBarLoveLevel)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val newPet = Pet(
                name = editName.text.toString(),
                scientificName = editScientificName.text.toString(),
                furType = editFurType.text.toString(),
                classification = editClassification.text.toString(),
                loveLevel = ratingBar.rating.toInt(),
                isFavorite = false
            )

            // Enviar datos de vuelta a MainActivity
            intent.putExtra("NEW_PET", newPet)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
