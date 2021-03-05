package com.cc12703.app.docsetreader.info

import java.io.File


data class PkgInfo private constructor(
        val name: String,
        val path: File
) {
    companion object {
        fun newPkgInfo(path: File): PkgInfo {
            val name = path.name.removeSuffix(".docset")
            return PkgInfo(name, path)
        }
    }
}
