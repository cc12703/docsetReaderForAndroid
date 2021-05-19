package com.cc12703.app.docsetreader.data.api.util

import com.cc12703.app.docsetreader.data.api.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseCallAdapterFactory : CallAdapter.Factory() {


    override fun get( returnType: Type,
                      annotations: Array<out Annotation>, retrofit: Retrofit
                    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != ApiResponse::class.java) {
            return null
        }

        val bodyType = getParameterUpperBound(0, returnType as ParameterizedType)
        return ApiResponseCallAdapter<Any>(bodyType)
    }
}