package com.cc12703.app.docsetreader.data.api

import com.cc12703.app.docsetreader.BuildConfig
import com.cc12703.app.docsetreader.data.api.info.GitHubRepo
import com.cc12703.app.docsetreader.data.api.info.GithubRelease
import com.cc12703.app.docsetreader.data.api.util.ApiResponseCallAdapterFactory
import com.jkyeo.basicparamsinterceptor.BasicParamsInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): ApiResponse<List<GitHubRepo>>

    @GET("repos/{owner}/{repo}/releases/latest")
    fun getLatestRel(@Path("owner") owner: String,
                     @Path("repo") repo: String): ApiResponse<GithubRelease>


    companion object {

        fun create(): GithubService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            var params = BasicParamsInterceptor.Builder()
                    .addHeaderParam("Accept", "application/vnd.github.v3+json")
                    .build()
            val client = OkHttpClient.Builder()
                    .addInterceptor(params)
                    .addInterceptor(logger)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BuildConfig.DOCSET_ACQUIRE_ADDR)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(ApiResponseCallAdapterFactory())
                    .build()
                    .create(GithubService::class.java)
        }
    }
}