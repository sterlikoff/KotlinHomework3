package ru.sterlikoff.hw3.components

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sterlikoff.hw3.models.AuthRequestParams
import ru.sterlikoff.hw3.models.RegistrationRequestParams

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

    suspend fun authenticate(request: AuthRequestParams) = API.authenticate(request)
    suspend fun registration(request: RegistrationRequestParams) = API.registration(request)

}