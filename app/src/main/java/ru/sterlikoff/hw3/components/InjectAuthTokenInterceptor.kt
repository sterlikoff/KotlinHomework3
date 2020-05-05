package ru.sterlikoff.hw3.components

import okhttp3.Interceptor
import okhttp3.Response

const val AUTH_TOKEN_HEADER = "Authorization"

class InjectAuthTokenInterceptor(private val authToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val requestWithToken = originalRequest.newBuilder()
            .header(AUTH_TOKEN_HEADER, "Bearer $authToken")
            .build()

        return chain.proceed(requestWithToken)

    }
}