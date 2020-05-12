package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.adapters.PostAdapter
import ru.sterlikoff.hw3.components.Repository
import ru.sterlikoff.hw3.interfaces.Item
import ru.sterlikoff.hw3.interfaces.PostEvents
import ru.sterlikoff.hw3.models.Post
import splitties.toast.toast

class FeedActivity : AppCompatActivity(R.layout.activity_feed), ActivityUI {

    override var dialog: ProgressDialog? = null

    private var offset = 0
    private val limit = 3

    private val postEvents = object : PostEvents {

        override fun share(post: Post) {

            lifecycleScope.launch {

                showProgress(this@FeedActivity)

                try {

                    if (Repository.share(post.id).isSuccessful) {
                        reset()
                    } else {
                        toast(getString(R.string.share_post_error_label))
                    }

                } catch (e: Exception) {
                    toast(R.string.connection_error_label)
                } finally {
                    hideProgress()
                }

            }

        }

        override fun next() {

            offset += limit
            fetch()

        }

    }

    private val postAdapter: PostAdapter = PostAdapter(mutableListOf(), this, postEvents)

    private fun reset() {

        offset = 0
        postAdapter.clear()
        fetch()

    }

    private fun fetch() {

        lifecycleScope.launch {

            showProgress(this@FeedActivity)

            try {

                val result = Repository.getPosts(limit, offset)
                swipe.isRefreshing = false

                if (result.isSuccessful) {

                    val list = result.body()?.map {
                        Post.fromInDto(it) as Item
                    }?.toList() ?: throw Exception("Server returned empty body")

                    postAdapter.add(list)

                } else {

                    if (result.code() == 401) {

                        logout(this@FeedActivity)
                        startActivity(Intent(this@FeedActivity, LoginActivity::class.java))
                        finish()

                    } else {

                        toast(getString(R.string.loading_data_error_label) + result.code())

                    }

                }

            } catch (e: Exception) {
                toast(getString(R.string.connection_error_label))
            } finally {
                hideProgress()
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        itemList.layoutManager = LinearLayoutManager(this)
        itemList.adapter = postAdapter

        supportActionBar?.title = "Лента"

        swipe.setOnRefreshListener {
            reset()
        }

        fetch()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.new_post, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =

        when (item.itemId) {

            R.id.btn_new_post -> {

                startActivity(Intent(this, PostActivity::class.java))
                false

            }

            else -> true

        }

}