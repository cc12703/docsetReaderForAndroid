package com.cc12703.app.docsetreader.util

import android.content.Context
import java.io.File

object PathUtil {


    fun buildPkgStorePath(ctx: Context): File {
        return ctx.getExternalFilesDir("github_docset")
    }


    fun buildPkgTempPath(ctx: Context): File {
        return ctx.getExternalFilesDir("temp_docset")
    }
}