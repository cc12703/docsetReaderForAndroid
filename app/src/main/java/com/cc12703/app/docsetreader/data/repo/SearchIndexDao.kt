package com.cc12703.app.docsetreader.data.repo

import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.AndroidConnectionSource
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import java.io.File

/*
room无法找到如何加载指定的db文件，所以使用了ormlite库
 */
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