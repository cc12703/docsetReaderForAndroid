package com.cc12703.app.docsetreader.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cc12703.app.docsetreader.info.PkgInfo

@Dao
abstract class PkgDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(pkg: PkgInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPkgs(pkgs: List<PkgInfo>)

    @Update
    abstract fun updatePkg(pkg: PkgInfo)

    @Query("SELECT * FROM pkg_info")
    abstract fun getAll(): LiveData<List<PkgInfo>>
}