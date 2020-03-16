package ru.sterlikoff.hw3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val post = Post("My first post", "Danill Sterlikov", 170484646)

        post.like()
        post.like()

        post.comment()
        post.comment()
        post.comment()

        post.rePost()

        val title = findViewById<TextView>(R.id.post_title)
        val date = findViewById<TextView>(R.id.post_date)
        val author = findViewById<TextView>(R.id.author)

        val likeCount = findViewById<TextView>(R.id.like_count)
        val commentCount = findViewById<TextView>(R.id.comment_count)
        val shareCount = findViewById<TextView>(R.id.share_count)

        val likeBtn = findViewById<ImageView>(R.id.btn_like)

        likeBtn.setOnClickListener {

            likeCount.setTextColor(resources.getColor(R.color.colorAccent))
            likeBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_thumb_up_accent_24dp))

        }

        title.text = post.title
        date.text = post.getAgoString()
        author.text = post.author
        likeCount.text = post.likeCount.toString()
        commentCount.text = post.commentCount.toString()
        shareCount.text = post.rePostCount.toString()

    }

}
