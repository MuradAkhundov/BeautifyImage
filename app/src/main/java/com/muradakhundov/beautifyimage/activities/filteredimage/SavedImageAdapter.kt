package com.muradakhundov.beautifyimage.activities.filteredimage

import android.app.AlertDialog
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muradakhundov.beautifyimage.databinding.ItemContainerSavedImageBinding
import com.muradakhundov.beautifyimage.listeners.SavedImageListener
import java.io.File

class SavedImageAdapter(private val savedImages : List<Pair<File,Bitmap>> , private val savedImageListener: SavedImageListener) : RecyclerView.Adapter<SavedImageAdapter.SavedImageViewHolder>() {

    inner class  SavedImageViewHolder(val binding : ItemContainerSavedImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding = ItemContainerSavedImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SavedImageViewHolder(binding)
    }

    override fun getItemCount(): Int = savedImages.size
    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        with(holder){
            with(savedImages[position]){
                binding.imageSaved.setImageBitmap(second)
                binding.imageSaved.setOnClickListener {
                    savedImageListener.onImageClicked(first)
                }

                binding.imageSaved.setOnLongClickListener {
                    showDeleteDialog(adapterPosition)
                    true
                }
            }
        }
    }

    private fun showDeleteDialog(position: Int) {
        val context = savedImageListener.getContext()
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete Image")
            .setMessage("Do you want to delete this image?")
            .setPositiveButton("Yes") { dialog, _ ->
                val file = savedImages[position].first
                file.delete() // Delete the file
                savedImages.toMutableList().removeAt(position)
                notifyItemRemoved(position)
                notifyDataSetChanged()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

}