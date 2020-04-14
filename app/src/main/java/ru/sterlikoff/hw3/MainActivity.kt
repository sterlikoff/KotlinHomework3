package ru.sterlikoff.hw3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sterlikoff.hw3.adapters.PostAdapter
import ru.sterlikoff.hw3.models.Post

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = mutableListOf<Post>()

        list.add(
            Post(
                "Is Video and Event Post",
                "Danill Sterlikov",
                170484646,
                15,
                82,
                3,
                33.1546,
                44.46847,
                "https://www.youtube.com/watch?v=WhWc3b3KhnY"
            )
        )

        list.add(
            Post(
                "Secondary post with very-very long title. Really very long title.",
                "Ivan Ivanov",
                170400000,
                7,
                81,
                15
            )
        )

        list.add(
            Post(
                "Is Event Post",
                "Kolya",
                170400999,
                71,
                810,
                1,
                33.1546,
                44.46847
            )
        )

        val sourcePost = Post(
            "Is only video Post",
            "Kolya",
            170400999,
            71,
            810,
            1,
            null,
            null,
            "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )

        list.add(sourcePost)

        list.add(
            Post(
                "Is sharing of previous",
                "Marya Petrosyan",
                170401000,
                3,
                1550,
                0,
                null,
                null,
                null,
                sourcePost
            )
        )

        list.add(
            Post(
                "Is Advertising",
                "Google",
                180400999,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                "https://google.com"
            )
        )

        val listView = findViewById<RecyclerView>(R.id.itemList)
        listView.adapter = PostAdapter(list, this)
        listView.layoutManager = LinearLayoutManager(this)


    }

}