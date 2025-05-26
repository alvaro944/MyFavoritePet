package edu.alvarocervantes.myfavoritepet.utils

import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import edu.alvarocervantes.myfavoritepet.model.Pet

object FirebaseBackupHelper {

    private val db = FirebaseFirestore.getInstance()

    fun backupPets(pets: List<Pet>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        if (pets.isEmpty()) {
            onFailure(Exception("No hay mascotas para hacer backup"))
            return
        }

        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        val collectionName = "MyFavouritesPets-$timestamp"

        val batch = db.batch()

        pets.forEach { pet ->
            val docRef = db.collection(collectionName).document(pet.id.toString())
            batch.set(docRef, pet)
        }

        val backupRef = db.collection("MyFavouritesPet-Backups").document("latest")
        val backupData = hashMapOf("collectionName" to collectionName, "timestamp" to timestamp)

        batch.commit()
            .addOnSuccessListener {
                backupRef.set(backupData)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getLatestBackupCollection(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("MyFavouritesPet-Backups")
            .document("latest")
            .get()
            .addOnSuccessListener { document ->
                val collectionName = document.getString("collectionName")
                if (!collectionName.isNullOrEmpty()) {
                    onSuccess(collectionName)
                } else {
                    onFailure(Exception("No se encontró un backup reciente en Firebase."))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun restorePets(
        collectionName: String,
        onSuccess: (List<Pet>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    onFailure(Exception("No hay datos en Firebase para restaurar."))
                    return@addOnSuccessListener
                }

                val pets = mutableListOf<Pet>()
                for (document in result) {
                    val pet = document.toObject(Pet::class.java)
                    pets.add(pet)
                }
                onSuccess(pets)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
