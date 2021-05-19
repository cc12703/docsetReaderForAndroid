package com.cc12703.app.docsetreader.data.api

import retrofit2.Response

sealed class ApiResponse<T> {



    companion object {

        fun <T> create(error: Throwable): ApiResponse<T> {
            return ApiFailResponse(error.message?: "unknown error")
        }

        fun <T> create(resp: Response<T>): ApiResponse<T> {
            return if(resp.isSuccessful) {
                val body = resp.body()
                if(body==null || resp.code()==204) ApiEmptyResponse() else ApiSuccessResponse(body = body)
            }
            else {
                val msg = resp.errorBody()?.string()
                val error = if(msg.isNullOrEmpty()) resp.message() else msg
                ApiFailResponse(error?: "unknown error")
            }
        }
    }
}


class ApiEmptyResponse<T> : ApiResponse<T>()


data class ApiSuccessResponse<T>(val body: T): ApiResponse<T>()


data class ApiFailResponse<T>(val message: String): ApiResponse<T>()