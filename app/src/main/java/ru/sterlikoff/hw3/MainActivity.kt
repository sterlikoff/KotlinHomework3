package ru.sterlikoff.hw3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import ru.sterlikoff.hw3.adapters.PostAdapter
import kotlinx.android.synthetic.main.activity_main.*
import ru.sterlikoff.hw3.components.Api

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    @KtorExperimentalAPI
    private fun fetch() = launch {

        val list = withContext(Dispatchers.IO) {
            Api.load("https://raw.githubusercontent.com/sterlikoff/PostGenerator/master/1.json")
        }

        itemList.adapter = PostAdapter(list, this@MainActivity)
        itemList.layoutManager = LinearLayoutManager(this@MainActivity)
        progressBar.visibility = View.GONE

    }

    @KtorExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetch()

    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}