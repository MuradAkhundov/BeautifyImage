package com.muradakhundov.beautifyimage.di

import com.muradakhundov.beautifyimage.repo.EditImageRepoImpl
import com.muradakhundov.beautifyimage.repo.EditImageRepository
import com.muradakhundov.beautifyimage.repo.SavedImagesRepository
import com.muradakhundov.beautifyimage.repo.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> {EditImageRepoImpl(androidContext())}
    factory<SavedImagesRepository> {SavedImagesRepositoryImpl(androidContext())}
}