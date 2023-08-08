package com.muradakhundov.beautifyimage.repo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.muradakhundov.beautifyimage.data.ImageFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorMatrixFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageRGBFilter
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception

class EditImageRepoImpl(private val context : Context) : EditImageRepository {
    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        val width = context.resources.displayMetrics.widthPixels
        val height = ((originalBitmap.height * width) / originalBitmap.width)
        return Bitmap.createScaledBitmap(originalBitmap,width,height,false)

        } ?: return null
    }

    override suspend fun getImageFilters(image: Bitmap): List<ImageFilter> {
        val gpuImage = GPUImage(context).apply {
            setImage(image)
        }
        val imageFilters : ArrayList<ImageFilter> = ArrayList()

        //Normal
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            val filter1 = ImageFilter("Normal",filter,gpuImage.bitmapWithFilterApplied)
            imageFilters.add(filter1)
        }


        //Sepia
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            val filter4 = ImageFilter("Sepia", filter, gpuImage.bitmapWithFilterApplied)
            imageFilters.add(filter4)
        }

        //Bright
        GPUImageRGBFilter(1.1f,1.3f,1.6f).also {filter ->
            gpuImage.setFilter(filter)
            val filter3 = ImageFilter("Bright",filter,gpuImage.bitmapWithFilterApplied)
            imageFilters.add(filter3)
        }

        //Yeli
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f , -0.3831f , 0.3883f,0.0f,
                0.0f, 1.0f , 0.2f , 0f,
                -0.1961f,0.0f,1.0f,0f,
                0f,0f,0f,1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            val filter2 = ImageFilter("Yeli", filter,gpuImage.bitmapWithFilterApplied)
            imageFilters.add(filter2)
        }

        GPUImageRGBFilter(0.8f, 0.4f, 0.2f).also { filter ->
            gpuImage.setFilter(filter)
            val filter5 = ImageFilter("Mars", filter, gpuImage.bitmapWithFilterApplied)
            imageFilters.add(filter5)
        }

        GPUImageRGBFilter(0.9f, 0.7f, 0.5f).also { filter ->
            gpuImage.setFilter(filter)
            val filter6 = ImageFilter("Retro", filter, gpuImage.bitmapWithFilterApplied)
            imageFilters.add(filter6)
        }


        GPUImageRGBFilter(0.3f, 0.8f, 1.0f).also { filter ->
            gpuImage.setFilter(filter)
            val filter7 = ImageFilter("Cool", filter, gpuImage.bitmapWithFilterApplied)
            imageFilters.add(filter7)
        }

        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.8f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.2f, 0.5f, 0.0f,
                0.0f, 0.5f, 1.5f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            val cyanToneFilter = ImageFilter("Cyan", filter, gpuImage.bitmapWithFilterApplied)
            imageFilters.add(cyanToneFilter)
        }

        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.5f, 0.0f, 0.8f, 0.0f,
                0.2f, 1.2f, 0.2f, 0.0f,
                0.8f, 0.0f, 1.5f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            val magentaBoostFilter = ImageFilter("Magenta", filter, gpuImage.bitmapWithFilterApplied)
            imageFilters.add(magentaBoostFilter)
        }


        GPUImageRGBFilter(1.0f, 0.7f, 0.3f).also { filter ->
            gpuImage.setFilter(filter)
            val amberShadeFilter = ImageFilter("Amber", filter, gpuImage.bitmapWithFilterApplied)
            imageFilters.add(amberShadeFilter)
        }
        return imageFilters
    }

    override suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri? {
        return try {
            val mediaStorageDirectory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Images"
            )
            if (!mediaStorageDirectory.exists()){
                mediaStorageDirectory.mkdirs()
            }
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(mediaStorageDirectory,fileName)
            saveFile(file,filteredBitmap)
            FileProvider.getUriForFile(context,"${context.packageName}.provider",file)
        }
        catch (e : Exception){
            null
        }
    }
    private fun saveFile(file : File,bitmap: Bitmap){
        with(FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,this)
            flush()
            close()
        }
    }

    private fun getInputStreamFromUri(uri: Uri) : InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

}