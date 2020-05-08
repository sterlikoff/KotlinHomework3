package ru.sterlikoff.hw3.components

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sterlikoff.hw3.models.AuthRequestParams
import ru.sterlikoff.hw3.models.RegistrationRequestParams
import ru.sterlikoff.hw3.models.dto.PostOutDto

object Repository {

    private const val server = "https://sterlikoff.herokuapp.com"

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(server)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private var API: API = retrofit.create(ru.sterlikoff.hw3.components.API::class.java)

    fun createRetrofitWithAuth(authToken: String) {

        val httpLoggerInterceptor = HttpLoggingInterceptor()
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(server)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        API = retrofit.create(ru.sterlikoff.hw3.components.API::class.java)

    }


    suspend fun authenticate(request: AuthRequestParams) = API.authenticate(request)
    suspend fun registration(request: RegistrationRequestParams) = API.registration(request)
    suspend fun getPosts(limit: Int, offset: Int) = API.getPosts(limit, offset)
    suspend fun addPost(request: PostOutDto) = API.addPost(request)
    suspend fun share(id: Int) = API.share(id)

}