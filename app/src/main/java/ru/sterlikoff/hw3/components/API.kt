package ru.sterlikoff.hw3.components

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthRequestParams(val username: String, val password: String)
data class RegistrationRequestParams(val username: String, val password: String)

data class Token(val token: String)

interface API {

    @POST("api/v1/authentication")
    suspend fun authenticate(@Body params: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun registration(@Body params: RegistrationRequestParams): Response<String>

}