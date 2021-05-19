package com.cc12703.app.docsetreader.data.api.util

import com.cc12703.app.docsetreader.data.api.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResponseCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, ApiResponse<R>> {

    override fun responseType() = responseType


    override fun adapt(call: Call<R>): ApiResponse<R> {
        try {
            return ApiResponse.create(call.execute())
        } catch (throwable: Throwable) {
            return ApiResponse.create(throwable)
        }

    }


}