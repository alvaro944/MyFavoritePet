package edu.alvarocervantes.myfavoritepet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PetAdapter(
    private val petList: MutableList<Pet>,
    private val onPetClick: (Pet) -> Unit,
    private val onDeleteClick: (Pet) -> Unit,
    private val onFavoriteClick: (Pet) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    inner class PetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPetName: TextView = view.findViewById(R.id.tvPetName)
        val tvPetCategory: TextView = view.findViewById(R.id.tvPetCategory)
        val ivPetImage: ImageView = view.findViewById(R.id.ivPetImage)
        val rbLoveLevel: RatingBar = view.findViewById(R.id.rbLoveLevel)
        val btnDeletePet: ImageView = view.findViewById(R.id.btnDeletePet)
        val btnFavoritePet: ImageView = view.findViewById(R.id.btnFavoritePet)

        init {
            view.setOnClickListener {
                onPetClick(petList[adapterPosition])
            }

            btnDeletePet.setOnClickListener {
                onDeleteClick(petList[adapterPosition])
            }

            btnFavoritePet.setOnClickListener {
                onFavoriteClick(petList[adapterPosition])
                notifyItemChanged(adapterPosition) // Actualiza la UI después del cambio
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]
        holder.tvPetName.text = pet.name
        holder.tvPetCategory.text = pet.category
        holder.rbLoveLevel.rating = pet.loveLevel.toFloat()

        Glide.with(holder.itemView.context)
            .load(pet.imageUri)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.ivPetImage)

        // Actualizar estado del favorito
        val favoriteIcon = if (pet.isFavorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        holder.btnFavoritePet.setImageResource(favoriteIcon)
    }

    override fun getItemCount(): Int = petList.size

    fun removePet(pet: Pet) {
        val position = petList.indexOf(pet)
        if (position != -1) {
            petList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
