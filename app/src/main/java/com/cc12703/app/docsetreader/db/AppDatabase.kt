package com.cc12703.app.docsetreader.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cc12703.app.docsetreader.info.PkgInfo


@Database(entities = [PkgInfo::class],
    version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun pkgDao(): PkgDao


    companion object {

        fun create(ctx: Context): AppDatabase {
            return Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}