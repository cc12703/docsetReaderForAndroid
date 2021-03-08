package com.cc12703.app.docsetreader.ui.read

import android.content.Context
import android.util.Log
import com.cc12703.app.docsetreader.data.PkgContentRepository
import com.cc12703.app.docsetreader.util.LOG_TAG
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import java.net.URLDecoder

class ReadWebView(
        context: Context,
        private val repo: PkgContentRepository
): WebView(context) {


    init {
        this.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(URLDecoder.decode(url, "utf-8"))
                return true
            }
        }

        val webSetting = this.settings
        webSetting.javaScriptEnabled = true
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.setAppCacheEnabled(true)
    }

    fun loadRelaUrl(url: String) {
        this.loadUrl(urlRelaToAbs(url))
    }

    private fun urlRelaToAbs(url: String): String {
        val file = repo.pageUrlToAbsPath(URLDecoder.decode(url, "utf-8"))
        val abs = "file://${file.absolutePath}"
        Log.i(LOG_TAG, "abs url: ${abs}")
        return abs
    }


}