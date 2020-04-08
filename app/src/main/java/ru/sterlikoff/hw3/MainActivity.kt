package ru.sterlikoff.hw3

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private fun render(post: Post) {

        val title = findViewById<TextView>(R.id.post_title)
        val date = findViewById<TextView>(R.id.post_date)
        val author = findViewById<TextView>(R.id.author)

        val likeCount = findViewById<TextView>(R.id.like_count)
        val commentCount = findViewById<TextView>(R.id.comment_count)
        val shareCount = findViewById<TextView>(R.id.share_count)

        val likeBtn = findViewById<ImageView>(R.id.btn_like)
        val locationBtn = findViewById<ImageView>(R.id.btn_location)
        val videoBtn = findViewById<ImageView>(R.id.btn_video)

        likeBtn.setOnClickListener {

            post.like()
            render(post)

        }

        locationBtn.setOnClickListener {

            startActivity(Intent().apply {
                this.action = Intent.ACTION_VIEW
                this.data = Uri.parse("geo:${post.lat},${post.lon}")
            })

        }

        videoBtn.setOnClickListener {

            startActivity(Intent().apply {
                this.action = Intent.ACTION_VIEW
                this.data = Uri.parse(post.videoUrl)
            })

        }

        title.text = post.title
        date.text = post.getAgoString()
        author.text = post.author

        if (post.likeCount == 0) {
            likeCount.visibility = View.INVISIBLE
        } else {
            likeCount.text = post.likeCount.toString()
        }

        commentCount.text = post.commentCount.toString()
        shareCount.text = post.rePostCount.toString()

        var colorId = R.color.gray;
        if (post.isLiked()) colorId = R.color.colorAccent

        var imageId = R.drawable.ic_thumb_up_ccc_24dp
        if (post.isLiked()) imageId = R.drawable.ic_thumb_up_accent_24dp

        likeCount.setTextColor(resources.getColor(colorId))
        likeBtn.setImageDrawable(resources.getDrawable(imageId))

        if (post.lon > 0 && post.lat > 0) {
            locationBtn.visibility = View.VISIBLE
        }

        if (!post.videoUrl.isEmpty()) {
            videoBtn.visibility = View.VISIBLE
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val post = Post(
            "My first post",
            "Danill Sterlikov",
            170484646,
            15,
            82,
            3,
            33.1546,
            44.46847,
            "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )

        render(post)

    }

}
