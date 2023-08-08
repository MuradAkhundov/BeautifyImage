package com.muradakhundov.beautifyimage.listeners

import com.muradakhundov.beautifyimage.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}