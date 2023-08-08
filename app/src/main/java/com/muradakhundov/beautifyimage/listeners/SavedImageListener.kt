package com.muradakhundov.beautifyimage.listeners

import android.content.Context
import java.io.File

interface SavedImageListener {
    fun onImageClicked(file : File)
    fun getContext(): Context
}