package com.cc12703.app.docsetreader.data.api.info

import com.google.gson.annotations.SerializedName

data class GithubRelease(
        @field:SerializedName("url")
        val url: String,
        @field:SerializedName("name")
        val name: String?,
        @field:SerializedName("body")
        val body: String?,
        @field:SerializedName("tag_name")
        val tag: String,
        @field:SerializedName("assets")
        val assets: List<Assets>?

) {
        data class Assets(
                @field:SerializedName("url")
                val url: String,
                @field:SerializedName("name")
                val name: String,
                @field:SerializedName("content_type")
                val type: String,
                @field:SerializedName("browser_download_url")
                val downloadUrl: String
        )
}
