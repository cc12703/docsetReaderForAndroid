package com.cc12703.app.docsetreader.data.dl

import java.io.File

interface DownloadPkgNotifyer {

    fun onSucc(file: File)

    fun onPercent(value: Int)
}