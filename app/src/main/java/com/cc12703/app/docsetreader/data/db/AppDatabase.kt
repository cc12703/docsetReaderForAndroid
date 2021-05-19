package com.cc12703.app.docsetreader.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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