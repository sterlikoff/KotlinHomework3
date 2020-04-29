package ru.sterlikoff.hw3

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.sterlikoff.hw3.adapters.PostAdapter

class MainActivity : MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList.layoutManager = LinearLayoutManager(this)
        itemList.adapter = PostAdapter(mutableListOf(), mutableListOf(), this)

    }

}