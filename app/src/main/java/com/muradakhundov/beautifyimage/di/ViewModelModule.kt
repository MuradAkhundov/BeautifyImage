package com.muradakhundov.beautifyimage.di

import com.muradakhundov.beautifyimage.viewmodel.EditImageViewModel
import com.muradakhundov.beautifyimage.viewmodel.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}
