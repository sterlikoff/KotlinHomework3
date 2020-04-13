package ru.sterlikoff.hw3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import ru.sterlikoff.hw3.adapters.PostAdapter
import kotlinx.android.synthetic.main.activity_main.*
import ru.sterlikoff.hw3.components.Api

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val server = "https://raw.githubusercontent.com/sterlikoff/PostGenerator/master"

    @KtorExperimentalAPI
    private fun fetch() = launch {

        progressBar.visibility = View.VISIBLE

        val mainList = withContext(Dispatchers.IO) {
            Api.load("$server/main.json")
        }

        val advList = withContext(Dispatchers.IO) {
            Api.load("$server/adv.json")
        }

        progressBar.visibility = View.GONE

        if (mainList != null && advList != null) {

            itemList.adapter = PostAdapter(mainList, advList, this@MainActivity)

        } else {
            Toast.makeText(this@MainActivity, getString(R.string.server_unavailable_message), Toast.LENGTH_LONG).show()
        }

    }

    @KtorExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList.layoutManager = LinearLayoutManager(this@MainActivity)

        fetch()

    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}