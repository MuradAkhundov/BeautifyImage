package com.muradakhundov.beautifyimage.activities.editimage


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.muradakhundov.beautifyimage.activities.filteredimage.FilteredImageActivity
import com.muradakhundov.beautifyimage.activities.main.MainActivity
import com.muradakhundov.beautifyimage.adapter.ImageFiltersAdapter
import com.muradakhundov.beautifyimage.data.ImageFilter
import com.muradakhundov.beautifyimage.databinding.ActivityEditImagesBinding
import com.muradakhundov.beautifyimage.listeners.ImageFilterListener
import com.muradakhundov.beautifyimage.util.displayToast
import com.muradakhundov.beautifyimage.util.show
import com.muradakhundov.beautifyimage.viewmodel.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImagesActivity : AppCompatActivity() , ImageFilterListener {

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }
    private lateinit var binding : ActivityEditImagesBinding
    private val viewModel : EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
    }

    private fun setupObservers(){
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let {bitmap ->
                //For the first time 'filtered image = original image '
                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }

                binding.imagePreview.setImageBitmap(bitmap)
                binding.imagePreview.show()
                viewModel.loadImageFilters(bitmap)
            } ?: kotlin.run {
                dataState.error?.let {error ->
                    displayToast(error)
                }
            }

        }
        viewModel.imageFiltersUiState.observe(this) {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters,this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
            filteredBitmap.observe(this) { bitmap ->
                binding.imagePreview.setImageBitmap(bitmap)
            }
        }

        viewModel.saveFilteredImageUiState.observe(this) {
            Log.e("observe","okay")
            val saveFilteredDataState = it ?: return@observe
            if (!saveFilteredDataState.isLoading) {
                binding.imageSave.visibility = View.GONE
                binding.savingProgress.visibility = View.VISIBLE
                Log.e("isloading","no")
            } else {
                binding.imageSave.visibility = View.VISIBLE
                binding.savingProgress.visibility = View.GONE
                Log.e("isloading","yes")

            }
            saveFilteredDataState.uri?.let { savedImageUri ->
                Log.e("savedImage","notNull")
                Intent(
                    applicationContext, FilteredImageActivity::class.java
                ).also { filteredImageIntent ->
                    Log.e("tag","error")
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filteredImageIntent)
                    finish()
                }
            } ?: kotlin.run {
                saveFilteredDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }
    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }



    private fun setListeners(){
        binding.imageBack.setOnClickListener {
            super.onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            Log.e("listener","successful")
            filteredBitmap.value?.let { bitmap->
                Log.e("value","isnotnull")
                viewModel.saveFilteredImage(bitmap)
            }
        }

        //long press to see original image for difference
        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onBackPressed() {

    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value= bitmapWithFilterApplied
            }
        }
    }
}