package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.adapters.PostAdapter
import ru.sterlikoff.hw3.components.Repository
import ru.sterlikoff.hw3.models.Post
import splitties.toast.toast

class MainActivity : AppCompatActivity(R.layout.activity_main), ActivityUI {

    override var dialog: ProgressDialog? = null

    private fun fetch() {

        lifecycleScope.launch {

            showProgress(this@MainActivity)
            val result = Repository.getPosts()
            hideProgress()

            if (result.isSuccessful) {

                val list: List<Post>? = result.body()?.map {
                    Post.fromInDto(it)
                }

                itemList.adapter = PostAdapter(list ?: mutableListOf(), mutableListOf(), this@MainActivity)

            } else {

                toast("При загрузке данных произошла ошибка " + result.code())

            }

        }

    }

    override fun onStart() {

        super.onStart()
        fetch()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        itemList.layoutManager = LinearLayoutManager(this)

    }

}