package com.cc12703.app.docsetreader.data.repo

import java.io.File

class PkgContentRepository private constructor(
        private val docsetFile: File
) {


    private val indexDao by lazy {
        SearchIndexDao.create(buildIndexDBFile(docsetFile))
    }



    fun getIndexPageUrl(): String {
        return indexDao.getByType("Guide").first().path!!
    }

    fun pageUrlToAbsPath(pageUrl: String): File {
        val root = File(docsetFile, "Contents/Resources/Documents")
        return File(root, pageUrl)
    }




    companion object {


        private fun buildIndexDBFile(docsetFile: File): File {
            return File(docsetFile, "Contents/Resources/docSet.dsidx")
        }


        private val instances: MutableMap<File, PkgContentRepository> = mutableMapOf()

        fun get(docsetFile: File): PkgContentRepository {
            synchronized(this) {
                var repo = instances.get(docsetFile)
                if (repo != null)
                    return repo

                repo = PkgContentRepository(docsetFile)
                instances.put(docsetFile, repo)
                return repo
            }
        }
    }




}