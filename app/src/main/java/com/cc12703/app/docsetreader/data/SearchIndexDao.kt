package com.cc12703.app.docsetreader.data

import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.AndroidConnectionSource
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import java.io.File


class SearchIndexDao private constructor(
        private val dao: Dao<SearchIndex, Int>
){


    fun getAll(): List<SearchIndex> = dao.queryForAll()
    fun getByType(type: String): List<SearchIndex> = dao.queryForEq("type", type)


    companion object {

        fun create(dbFile: File): SearchIndexDao {
            val cSource = AndroidConnectionSource(SQLiteDatabase.openOrCreateDatabase(dbFile, null))
            return SearchIndexDao(DaoManager.createDao(cSource, SearchIndex::class.java))
        }

    }

}