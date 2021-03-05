package com.cc12703.app.docsetreader.data

import android.os.Environment
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PkgsRepository @Inject constructor() {



    fun getPkgList(): PkgLiveData {
       return PkgLiveData(File(Environment.getExternalStorageDirectory(), "dash_docset"))
    }

}