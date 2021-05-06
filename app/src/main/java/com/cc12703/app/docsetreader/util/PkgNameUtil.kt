package com.cc12703.app.docsetreader.util

//name format: name-version.docset
object PkgNameUtil {


    val SUFFIX_NAME = "docset"

    fun build(name: String, ver: String): String {
        return "${name}-${ver}.${SUFFIX_NAME}"
    }

    fun parse(dirName: String): Pair<String, String> {
        val items = dirName.removeSuffix(".${SUFFIX_NAME}").split("-")
        return Pair(items[0], items[1])
    }


}