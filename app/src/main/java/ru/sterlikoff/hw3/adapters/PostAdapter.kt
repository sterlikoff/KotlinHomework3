package ru.sterlikoff.hw3.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.sterlikoff.hw3.R
import ru.sterlikoff.hw3.interfaces.Item
import ru.sterlikoff.hw3.interfaces.PostEvents
import ru.sterlikoff.hw3.models.NextButton
import ru.sterlikoff.hw3.models.Post
import splitties.exceptions.illegal

class PostAdapter(

    private val list: MutableList<Item>,
    private val context: Context,
    private val events: PostEvents

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.post_title)
        val content: TextView = itemView.findViewById(R.id.post_content)
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
        val shareBtn: ImageView = itemView.findViewById(R.id.share_btn)

    }

    class NextButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.button_next)
    }

    fun add(elements: List<Item>) {

        list.addAll(elements)
        list.add(NextButton())
        notifyDataSetChanged()

    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =

        when (viewType) {

            TYPE_POST -> PostViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_post,
                    parent,
                    false
                )
            ).apply {

                likeBtn.setOnClickListener {

                    val post = list[adapterPosition] as Post
                    post.like()
                    notifyItemChanged(adapterPosition, LikeChange(post.likeCount))

                }

                locationBtn.setOnClickListener {

                    val post = list[adapterPosition] as Post

                    context.startActivity(Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse("geo:${post.lat},${post.lon}")
                    })

                }

                videoBtn.setOnClickListener {

                    val post = list[adapterPosition] as Post

                    context.startActivity(Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(post.videoUrl)
                    })

                }

                itemView.setOnClickListener {

                    val post = list[adapterPosition] as Post
                    if (post.advertUrl != null) {

                        context.startActivity(Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = Uri.parse(post.advertUrl)
                        })

                    }

                }

                shareBtn.setOnClickListener {
                    val post = list[adapterPosition] as Post
                    events.share(post)
                }

            }

            TYPE_NEXT -> NextButtonViewHolder(

                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_next,
                    parent,
                    false
                )

            ).apply {

                button.setOnClickListener {
                    list.removeAt(adapterPosition)
                    events.next()
                }

            }

            else -> illegal()

        }

    override fun getItemCount() = list.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) = when (holder.itemViewType) {

            TYPE_POST -> onBindPost(holder as PostViewHolder, position, payloads)
            TYPE_NEXT -> {
            }
            else -> illegal()

        }

    private fun renderLikeBtn(isLiked: Boolean, likeCount: Int, holder: PostViewHolder) {

        if (likeCount == 0) {
            holder.likeCount.visibility = View.INVISIBLE
        } else {
            holder.likeCount.text = likeCount.toString()
        }

        var colorId = R.color.gray
        if (isLiked) colorId = R.color.colorAccent

        var imageId = R.drawable.ic_thumb_up_ccc_24dp
        if (isLiked) imageId = R.drawable.ic_thumb_up_accent_24dp

        holder.likeCount.setTextColor(context.resources.getColor(colorId))
        holder.likeBtn.setImageDrawable(context.resources.getDrawable(imageId))

    }

    private fun onBindPost(holder: PostViewHolder, position: Int, payloads: MutableList<Any>) {

        if (payloads.isNotEmpty()) {

            payloads.forEach { change ->
                if (change is LikeChange) {
                    renderLikeBtn(true, change.likes, holder)
                }
            }

            return
        }

        var post = list[position] as Post

        if (post.parent !== null) {

            val new = post
            post = post.parent!!

            holder.targetLayout.visibility = View.VISIBLE
            holder.targetAuthor.text = new.author
            holder.targetDate.text = new.getAgoString(context)

        }

        holder.title.text = post.title
        holder.date.text = post.getAgoString(context)
        holder.author.text = post.author
        holder.content.text = post.content

        holder.commentCount.text = post.commentCount.toString()
        holder.shareCount.text = post.rePostCount.toString()

        renderLikeBtn(post.isLiked(), post.likeCount, holder)

        if (post.lon !== null && post.lat !== null) {
            holder.locationBtn.visibility = View.VISIBLE
        }

        if (post.videoUrl !== null) {
            holder.videoBtn.visibility = View.VISIBLE
        }

    }

    override fun getItemViewType(position: Int): Int =

        when (list[position]) {

            is Post -> TYPE_POST
            is NextButton -> TYPE_NEXT
            else -> illegal()

        }

    companion object {
        private const val TYPE_POST = 1
        private const val TYPE_NEXT = 2
    }

    private data class LikeChange(val likes: Int)

}