package ru.sterlikoff.hw3.components

import retrofit2.Response
import retrofit2.http.*
import ru.sterlikoff.hw3.models.AuthRequestParams
import ru.sterlikoff.hw3.models.RegistrationRequestParams
import ru.sterlikoff.hw3.models.dto.PostInDto
import ru.sterlikoff.hw3.models.dto.PostOutDto

data class Token(val token: String)

interface API {

    @POST("api/v1/authentication")
    suspend fun authenticate(@Body params: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun registration(@Body params: RegistrationRequestParams): Response<String>

    @GET("api/v1/posts")
    suspend fun getPosts(@Query("limit") limit: Int, @Query("offset") offset: Int): Response<List<PostInDto>>

    @POST("api/v1/posts/create")
    suspend fun addPost(@Body params: PostOutDto): Response<PostInDto>

    @GET("/api/v1/posts/share/{id}")
    suspend fun share(@Path("id") id: Int): Response<PostInDto>

}