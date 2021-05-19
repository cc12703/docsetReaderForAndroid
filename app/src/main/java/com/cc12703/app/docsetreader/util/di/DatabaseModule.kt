package com.cc12703.app.docsetreader.util.di

import android.content.Context
import com.cc12703.app.docsetreader.data.db.AppDatabase
import com.cc12703.app.docsetreader.data.db.PkgDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
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