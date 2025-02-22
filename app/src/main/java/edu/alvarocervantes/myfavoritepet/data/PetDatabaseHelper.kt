package edu.alvarocervantes.myfavoritepet.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import edu.alvarocervantes.myfavoritepet.model.Pet

class PetDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_PETS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_SCIENTIFIC_NAME TEXT,
                $COLUMN_FUR_TYPE TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_IMAGE_URI TEXT,
                $COLUMN_LOVE_LEVEL INTEGER,
                $COLUMN_IS_FAVORITE INTEGER,
                $COLUMN_WIKI_LINK TEXT
            );
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PETS")
        onCreate(db)
    }

    // Insertar una nueva mascota
    fun insertPet(pet: Pet): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, pet.name)
            put(COLUMN_SCIENTIFIC_NAME, pet.scientificName)
            put(COLUMN_FUR_TYPE, pet.furType)
            put(COLUMN_CATEGORY, pet.category)
            put(COLUMN_IMAGE_URI, pet.imageUri)
            put(COLUMN_LOVE_LEVEL, pet.loveLevel)
            put(COLUMN_IS_FAVORITE, if (pet.isFavorite) 1 else 0)
            put(COLUMN_WIKI_LINK, pet.wikiLink)
        }
        return db.insert(TABLE_PETS, null, values)
    }

    // Obtener todas las mascotas
    fun getAllPets(): List<Pet> {
        val petList = mutableListOf<Pet>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PETS", null)

        while (cursor.moveToNext()) {
            val pet = Pet(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                scientificName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCIENTIFIC_NAME)),
                furType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FUR_TYPE)),
                category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI)),
                loveLevel = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOVE_LEVEL)),
                isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1,
                wikiLink = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WIKI_LINK))
            )
            petList.add(pet)
        }
        cursor.close()
        return petList
    }

    // Eliminar una mascota
    fun deletePet(petId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_PETS, "$COLUMN_ID = ?", arrayOf(petId.toString()))
    }

    // Actualizar una mascota
    fun updatePet(pet: Pet): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, pet.name)
            put(COLUMN_SCIENTIFIC_NAME, pet.scientificName)
            put(COLUMN_FUR_TYPE, pet.furType)
            put(COLUMN_CATEGORY, pet.category)
            put(COLUMN_IMAGE_URI, pet.imageUri)
            put(COLUMN_LOVE_LEVEL, pet.loveLevel)
            put(COLUMN_IS_FAVORITE, if (pet.isFavorite) 1 else 0)
            put(COLUMN_WIKI_LINK, pet.wikiLink)
        }
        return db.update(TABLE_PETS, values, "$COLUMN_ID = ?", arrayOf(pet.id.toString()))
    }

    companion object {
        private const val DATABASE_NAME = "pets.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_PETS = "pets"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SCIENTIFIC_NAME = "scientificName"
        const val COLUMN_FUR_TYPE = "furType"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_IMAGE_URI = "imageUri"
        const val COLUMN_LOVE_LEVEL = "loveLevel"
        const val COLUMN_IS_FAVORITE = "isFavorite"
        const val COLUMN_WIKI_LINK = "wikiLink"
    }
}
