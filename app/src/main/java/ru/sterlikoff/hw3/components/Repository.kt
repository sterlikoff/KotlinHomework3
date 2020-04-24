package ru.sterlikoff.hw3.components

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Repository {

    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl("https://sterlikoff.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    private val API: API by lazy {

        retrofit.create(
            ru.sterlikoff.hw3.components.API::class.java
        )

    }

    suspend fun authenticate(login: String, password: String) =
        API.authenticate(
            AuthRequestParams(login, password)
        )

    suspend fun registration(login: String, password: String) =
        API.registration(
            RegistrationRequestParams(login, password)
        )

}