package edu.alvarocervantes.myfavoritepet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditPetActivity : AppCompatActivity() {

    private var petToEdit: Pet? = null
    private var selectedImageUri: Uri? = null
    private var photoFile: File? = null
    private lateinit var ivPetImage: ImageView

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
        val btnTakePhoto = findViewById<Button>(R.id.btnTakePhoto)
        val btnSave = findViewById<Button>(R.id.btnSave)
        ivPetImage = findViewById(R.id.ivPetImage)

        // Recibir la mascota si estamos en modo edición
        petToEdit = intent.getSerializableExtra("EDIT_PET") as? Pet
        if (petToEdit != null) {
            etName.setText(petToEdit!!.name)
            etScientificName.setText(petToEdit!!.scientificName)
            etFurType.setText(petToEdit!!.furType)
            etCategory.setText(petToEdit!!.category)
            etWikiLink.setText(petToEdit!!.wikiLink)
            rbLoveLevel.rating = petToEdit!!.loveLevel.toFloat()

            // Restaurar la imagen si ya tenía una
            if (!petToEdit!!.imageUri.isNullOrEmpty()) {
                selectedImageUri = Uri.parse(petToEdit!!.imageUri)
                ivPetImage.setImageURI(selectedImageUri)
            }
        }

        btnAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GALLERY)
        }

        btnTakePhoto.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        btnSave.setOnClickListener {
            if (etName.text.isEmpty() || etCategory.text.isEmpty()) {
                Toast.makeText(this, "Nombre y categoría son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedPet = Pet(
                id = petToEdit?.id ?: (0..1000000).random(),
                name = etName.text.toString(),
                scientificName = etScientificName.text.toString(),
                furType = etFurType.text.toString(),
                category = etCategory.text.toString(),
                imageUri = selectedImageUri?.toString() ?: petToEdit?.imageUri,
                loveLevel = rbLoveLevel.rating.toInt(),
                isFavorite = petToEdit?.isFavorite ?: false,
                wikiLink = etWikiLink.text.toString()
            )

            val resultIntent = Intent().apply {
                putExtra("NEW_PET", updatedPet)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhoto()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()

        photoFile?.let {
            val photoURI = FileProvider.getUriForFile(
                this,
                "edu.alvarocervantes.myfavoritepet.fileprovider",
                it
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PET_$timeStamp", ".jpg", storageDir).apply {
            selectedImageUri = Uri.fromFile(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    selectedImageUri = data?.data
                    ivPetImage.setImageURI(selectedImageUri)
                }
                REQUEST_IMAGE_CAPTURE -> {
                    selectedImageUri = Uri.fromFile(photoFile)
                    val bitmap = BitmapFactory.decodeFile(photoFile?.absolutePath)
                    ivPetImage.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 200
        private const val REQUEST_IMAGE_CAPTURE = 101
        private const val REQUEST_GALLERY = 100
    }
}
