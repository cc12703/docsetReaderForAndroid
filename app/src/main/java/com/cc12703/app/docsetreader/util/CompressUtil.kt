package com.cc12703.app.docsetreader.util

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

object CompressUtil {


    fun uncompressTarGzipFile(src: File, dstPath: File) {
        TarArchiveInputStream(GzipCompressorInputStream(
            BufferedInputStream(FileInputStream(src)))).use { tarIns ->
            var entry: TarArchiveEntry? = tarIns.nextTarEntry
            if(entry == null)
                return

            while (entry != null) {
                if(entry.isDirectory) {
                    File(dstPath, entry.name).mkdirs()
                }
                else {
                    val data = ByteArray(tarIns.available())
                    tarIns.read(data)
                    File(dstPath, entry.name).writeBytes(data)
                }

                entry = tarIns.nextTarEntry
            }
        }
    }
}