package com.muradakhundov.beautifyimage.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.muradakhundov.beautifyimage.activities.editimage.EditImagesActivity
import com.muradakhundov.beautifyimage.activities.savedimages.SavedImagesActivity
import com.muradakhundov.beautifyimage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
    }

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setListeners()
        setContentView(binding.root)
    }


    private fun setListeners(){
        binding.editNewImagesBtn.setOnClickListener {
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ).also { pickerIntent ->
                pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(pickerIntent, REQUEST_CODE_PICK_IMAGE)
            }
        }

        binding.savedImagesBtn.setOnClickListener {
            Intent(applicationContext,SavedImagesActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK){
            data?.data?.let {
                imageUri ->
                Intent(applicationContext, EditImagesActivity::class.java).also { editImageIntent ->
                    editImageIntent.putExtra(KEY_IMAGE_URI,imageUri)
                    startActivity(editImageIntent)

                }
            }
        }
    }


}