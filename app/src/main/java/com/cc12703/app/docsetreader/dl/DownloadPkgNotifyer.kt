package com.cc12703.app.docsetreader.dl

import com.cc12703.app.docsetreader.info.PkgInfo
import java.io.File

interface DownloadPkgNotifyer {

    fun onSucc(file: File)

    fun onPercent(value: Int)
}