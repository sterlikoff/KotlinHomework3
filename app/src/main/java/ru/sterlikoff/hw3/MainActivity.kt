package ru.sterlikoff.hw3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.http.ContentType
import kotlinx.coroutines.*
import ru.sterlikoff.hw3.adapters.PostAdapter
import ru.sterlikoff.hw3.models.Post

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private fun fetch() = launch {

        val list = withContext(Dispatchers.IO) {

            val url =
                "https://raw.githubusercontent.com/sterlikoff/PostGenerator/master/1.json"
            val client = HttpClient {
                install(JsonFeature) {
                    acceptContentTypes = listOf(
                        ContentType.Text.Plain,
                        ContentType.Application.Json
                    )
                    serializer = GsonSerializer()

                }
            }

            val res = client.get<List<Post>>(url)
            client.close()

            res

        }

        val listView = findViewById<RecyclerView>(R.id.itemList)
        listView.adapter = PostAdapter(list, this@MainActivity)
        listView.layoutManager = LinearLayoutManager(this@MainActivity)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetch()

    }

}