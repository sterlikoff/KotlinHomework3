package ru.sterlikoff.hw3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sterlikoff.hw3.adapters.PostAdapter
import ru.sterlikoff.hw3.interfaces.Item
import ru.sterlikoff.hw3.models.Post

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = mutableListOf<Item>()

        list.add(Post(
            "My first post",
            "Danill Sterlikov",
            170484646,
            15,
            82,
            3,
            33.1546,
            44.46847,
            "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        ))

        list.add(Post(
            "Secondary post with very-very long title. Really very long title.",
            "Ivan Ivanov",
            170400000,
            7,
            81,
            15
        ))

        list.add(Post(
            "Third!",
            "Kolya",
            170400999,
            71,
            810,
            1,
            33.1546,
            44.46847
        ))

        val listView = findViewById<RecyclerView>(R.id.itemList)
        listView.adapter = PostAdapter(list, this)
        listView.layoutManager = LinearLayoutManager(this)


    }

}
