package ru.sterlikoff.hw3.components

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.util.KtorExperimentalAPI
import ru.sterlikoff.hw3.models.Post

class Api {

    companion object {

        @KtorExperimentalAPI
        suspend fun load(url: String): List<Post>? {

            val client = HttpClient {
                install(JsonFeature) {
                    acceptContentTypes = listOf(
                        ContentType.Text.Plain,
                        ContentType.Application.Json
                    )
                    serializer = GsonSerializer()

                }
            }

            try {

                val res = client.get<List<Post>>(url)
                client.close()

                return res

            } catch (e: Throwable) {

                Log.i("error", e.message ?: "")

            }

            return null
        }

    }

}