package com.muradakhundov.beautifyimage.activities.savedimages

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.muradakhundov.beautifyimage.R
import com.muradakhundov.beautifyimage.activities.editimage.EditImagesActivity
import com.muradakhundov.beautifyimage.activities.filteredimage.FilteredImageActivity
import com.muradakhundov.beautifyimage.activities.filteredimage.SavedImageAdapter
import com.muradakhundov.beautifyimage.databinding.ActivitySavedImagesBinding
import com.muradakhundov.beautifyimage.listeners.SavedImageListener
import com.muradakhundov.beautifyimage.util.displayToast
import com.muradakhundov.beautifyimage.viewmodel.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity() , SavedImageListener {

    private lateinit var binding : ActivitySavedImagesBinding
    private val viewModel : SavedImagesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setUpObserver(){
        viewModel.savedImagesUiState.observe(this) {
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let { savedImages ->
                SavedImageAdapter(savedImages,this).also { adapter ->
                    with(binding.savedImagesRv){
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {

    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )

        Intent(
            applicationContext,
            FilteredImageActivity::class.java,
        ).also { filteredImageIntent ->
            filteredImageIntent.putExtra(EditImagesActivity.KEY_FILTERED_IMAGE_URI,fileUri)
            startActivity(filteredImageIntent)
        }
    }
    override fun getContext(): Context = this
}