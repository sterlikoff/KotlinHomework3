package ru.sterlikoff.hw3

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.adapters.PostAdapter
import ru.sterlikoff.hw3.components.Repository
import ru.sterlikoff.hw3.models.Post

class MainActivity : MyActivity() {

    private fun fetch() {

        launch {

            showProgress()
            val result = Repository.getPosts()
            hideProgress()

            Log.i("responseCode", result.code().toString())

            if (result.isSuccessful) {

                val list: List<Post>? = result.body()?.map {
                    Post.fromInDto(it)
                }

                itemList.adapter = PostAdapter(list ?: mutableListOf(), mutableListOf(), this@MainActivity)

            } else {

                showMessage("При загрузке данных произошла ошибка " + result.code())

            }

        }

    }

    override fun onStart() {

        super.onStart()
        fetch()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList.layoutManager = LinearLayoutManager(this)

    }

}