package com.cc12703.app.docsetreader.info

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cc12703.app.docsetreader.util.PathUtil
import com.cc12703.app.docsetreader.util.PkgNameUtil
import java.io.File


@Entity(tableName = "pkg_info", primaryKeys = ["name"])
data class PkgInfo(
        @ColumnInfo(name="name")
        var name: String,
) {

    @ColumnInfo(name="local_ver")
    var localVer: String = ""

    @ColumnInfo(name="last_ver")
    var lastVer: String = ""

    @ColumnInfo(name="last_url")
    var lastUrl: String = ""


    @ColumnInfo(name="dl_ver")
    var dlVer: String = ""

    @ColumnInfo(name="dl_url")
    var dlUrl: String = ""

    @ColumnInfo(name="dl_percent")
    var dlPercent: Int = 0


    fun isShow(): Boolean {
        return localVer.isNotEmpty()
    }

    fun isDownload(): Boolean {
        if(lastVer.isEmpty())
            return false

        if(localVer.isEmpty())
            return true

        return (lastVer != localVer)
    }

    fun isDownloading(): Boolean {
        return dlVer.isNotEmpty()
    }


    fun buildLocalPkg(ctx: Context): File {
        return File(PathUtil.buildPkgStorePath(ctx),
                    PkgNameUtil.build(name, localVer))
    }

    companion object {

        fun create(remote: PkgRemoteInfo): PkgInfo {
            return PkgInfo(remote.name).apply {
                this.lastVer = remote.ver
                this.lastUrl = remote.url
            }
        }

    }


}
