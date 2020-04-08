package ru.sterlikoff.hw3.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.sterlikoff.hw3.R
import ru.sterlikoff.hw3.interfaces.Item
import ru.sterlikoff.hw3.models.Post
import java.lang.IllegalArgumentException

class PostAdapter(private var list: List<Item>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.post_title)
        val date: TextView = itemView.findViewById(R.id.post_date)
        val author: TextView = itemView.findViewById(R.id.author)

        val targetLayout: ViewGroup = itemView.findViewById(R.id.targetPostLayout)
        val targetAuthor: TextView = itemView.findViewById(R.id.new_author)
        val targetDate: TextView = itemView.findViewById(R.id.new_post_date)

        val likeCount: TextView = itemView.findViewById(R.id.like_count)
        val commentCount: TextView = itemView.findViewById(R.id.comment_count)
        val shareCount: TextView = itemView.findViewById(R.id.share_count)

        val likeBtn: ImageView = itemView.findViewById(R.id.btn_like)
        val locationBtn: ImageView = itemView.findViewById(R.id.btn_location)
        val videoBtn: ImageView = itemView.findViewById(R.id.btn_video)
        val hideBtn: ImageView = itemView.findViewById(R.id.btn_hide)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =

        when (viewType) {

            TYPE_POST -> PostViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException()

        }

    override fun getItemCount() = list.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =

        when (holder.itemViewType) {

            TYPE_POST -> onBindPost(holder, position)
            else -> throw IllegalArgumentException()

        }

    private fun onBindPost(holder: RecyclerView.ViewHolder, position: Int) {

        val postHolder = holder as PostViewHolder
        var post = list[position]

        require(post is Post)

        if (post.parent !== null) {

            val new = post
            post = post.parent!!

            postHolder.targetLayout.visibility = View.VISIBLE
            postHolder.targetAuthor.text = new.author
            postHolder.targetDate.text = new.getAgoString()

        }

        postHolder.title.text = post.title
        postHolder.date.text = post.getAgoString()
        postHolder.author.text = post.author

        if (post.likeCount == 0) {
            postHolder.likeCount.visibility = View.INVISIBLE
        } else {
            postHolder.likeCount.text = post.likeCount.toString()
        }

        postHolder.commentCount.text = post.commentCount.toString()
        postHolder.shareCount.text = post.rePostCount.toString()

        var colorId = R.color.gray;
        if (post.isLiked()) colorId = R.color.colorAccent

        var imageId = R.drawable.ic_thumb_up_ccc_24dp
        if (post.isLiked()) imageId = R.drawable.ic_thumb_up_accent_24dp

        postHolder.likeCount.setTextColor(context.resources.getColor(colorId))
        postHolder.likeBtn.setImageDrawable(context.resources.getDrawable(imageId))

        if (post.lon !== null && post.lat !== null) {
            postHolder.locationBtn.visibility = View.VISIBLE
        }

        if (post.videoUrl !== null) {
            postHolder.videoBtn.visibility = View.VISIBLE
        }

        postHolder.likeBtn.setOnClickListener {

            post.like()
            notifyDataSetChanged()

        }

        postHolder.locationBtn.setOnClickListener {

            context.startActivity(Intent().apply {
                this.action = Intent.ACTION_VIEW
                this.data = Uri.parse("geo:${post.lat},${post.lon}")
            })

        }

        postHolder.videoBtn.setOnClickListener {

            context.startActivity(Intent().apply {
                this.action = Intent.ACTION_VIEW
                this.data = Uri.parse(post.videoUrl)
            })

        }

        postHolder.hideBtn.setOnClickListener {

            list = list.filter {
                it.hashCode() != post.hashCode()
            }

            notifyDataSetChanged()

        }

        if (post.advertUrl !== null) {

            postHolder.itemView.setOnClickListener {

                context.startActivity(Intent().apply {
                    this.action = Intent.ACTION_VIEW
                    this.data = Uri.parse(post.advertUrl)
                })

            }

        }

    }

    override fun getItemViewType(position: Int): Int =

        when (list[position]) {

            is Post -> TYPE_POST
            else -> throw IllegalArgumentException()

        }

    companion object {
        private const val TYPE_POST = 1
    }

}