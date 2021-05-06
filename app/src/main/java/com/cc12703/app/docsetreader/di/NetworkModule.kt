package com.cc12703.app.docsetreader.di

import com.cc12703.app.docsetreader.api.GithubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGithubService(): GithubService {
        return GithubService.create()
    }
}