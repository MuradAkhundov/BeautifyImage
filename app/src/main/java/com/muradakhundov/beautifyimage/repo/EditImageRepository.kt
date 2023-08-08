package com.muradakhundov.beautifyimage.repo

import android.graphics.Bitmap
import android.net.Uri
import com.muradakhundov.beautifyimage.data.ImageFilter

interface EditImageRepository {

    suspend fun prepareImagePreview(imageUri: Uri) : Bitmap?
    suspend fun getImageFilters(image : Bitmap) : List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap : Bitmap)  :Uri?
}