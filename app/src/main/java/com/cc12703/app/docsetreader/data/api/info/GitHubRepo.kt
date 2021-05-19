package com.cc12703.app.docsetreader.data.api.info

import com.google.gson.annotations.SerializedName

data class GitHubRepo (
        @field:SerializedName("id")
        val id: Int,
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("full_name")
        val fullName: String,
        @field:SerializedName("description")
        val desc: String?,
        @field:SerializedName("private")
        val isPrivate: Boolean,
        @field:SerializedName("fork")
        val isFork: Boolean,
        @field:SerializedName("owner")
        val owner: Owner,
){

        data class Owner(
                @field:SerializedName("login")
                val login: String,
                @field:SerializedName("url")
                val url: String?
        )
}