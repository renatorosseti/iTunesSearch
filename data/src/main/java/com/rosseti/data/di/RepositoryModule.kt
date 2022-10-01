package com.rosseti.data.di

import com.rosseti.data.repository.ITunesRepositoryImpl
import com.rosseti.domain.repository.ITunesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun provideITunesRepository(repository: ITunesRepositoryImpl) : ITunesRepository
}