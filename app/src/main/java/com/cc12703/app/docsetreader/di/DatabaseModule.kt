package com.cc12703.app.docsetreader.di

import android.content.Context
import com.cc12703.app.docsetreader.db.AppDatabase
import com.cc12703.app.docsetreader.db.PkgDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.create(context)
    }

    @Singleton
    @Provides
    fun providePkgDao(db: AppDatabase): PkgDao {
        return db.pkgDao()
    }
}