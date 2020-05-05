package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.components.Repository
import ru.sterlikoff.hw3.models.dto.PostOutDto
import splitties.toast.toast

class PostActivity : AppCompatActivity(R.layout.activity_post), ActivityUI {

    override var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        super.onCreate(savedInstanceState, persistentState)
        supportActionBar?.title = "Новый пост"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.save_post, menu)
        return true

    }

    private fun savePost() = lifecycleScope.launch {

            val post = PostOutDto(
                editTitle.text.toString()
            )

            showProgress(this@PostActivity)
            val response = Repository.addPost(post)
            hideProgress()

            if (response.isSuccessful) {

                finish()

            } else {

                toast(R.string.error_label)

            }

        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {

            R.id.btn_save_post -> {

                savePost()
                false

            }
            else -> true

        }

}