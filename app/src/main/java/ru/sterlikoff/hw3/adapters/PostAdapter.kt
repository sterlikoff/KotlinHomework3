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
import java.lang.IllegalArgumentException

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
        val hideBtn: ImageView = itemView.findViewById(R.id.btn_hide)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =

        when (viewType) {

            TYPE_POST -> PostViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_post,
                    parent,
                    false
                )

            )

            TYPE_NEXT -> NextButtonViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_item_next,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException()

        }

    override fun getItemCount() = list.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =

        when (holder.itemViewType) {

            TYPE_POST -> onBindPost(holder as PostViewHolder, position)
            TYPE_NEXT -> onBindNext(holder as NextButtonViewHolder, position)
            else -> illegal()

        }

    private fun onBindNext(holder: NextButtonViewHolder, position: Int) {

        holder.button.setOnClickListener {
            list.removeAt(position)
            events.next()
        }

    }

    private fun onBindPost(holder: PostViewHolder, position: Int) {

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

        if (post.likeCount == 0) {
            holder.likeCount.visibility = View.INVISIBLE
        } else {
            holder.likeCount.text = post.likeCount.toString()
        }

        holder.commentCount.text = post.commentCount.toString()
        holder.shareCount.text = post.rePostCount.toString()

        var colorId = R.color.gray;
        if (post.isLiked()) colorId = R.color.colorAccent

        var imageId = R.drawable.ic_thumb_up_ccc_24dp
        if (post.isLiked()) imageId = R.drawable.ic_thumb_up_accent_24dp

        holder.likeCount.setTextColor(context.resources.getColor(colorId))
        holder.likeBtn.setImageDrawable(context.resources.getDrawable(imageId))

        if (post.lon !== null && post.lat !== null) {
            holder.locationBtn.visibility = View.VISIBLE
        }

        if (post.videoUrl !== null) {
            holder.videoBtn.visibility = View.VISIBLE
        }

        holder.likeBtn.setOnClickListener {

            post.like()
            notifyDataSetChanged()

        }

        holder.locationBtn.setOnClickListener {

            context.startActivity(Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("geo:${post.lat},${post.lon}")
            })

        }

        holder.videoBtn.setOnClickListener {

            context.startActivity(Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(post.videoUrl)
            })

        }

        holder.hideBtn.setOnClickListener {

            list.remove(post)
            notifyDataSetChanged()

        }

        if (post.advertUrl !== null) {

            holder.itemView.setOnClickListener {

                context.startActivity(Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(post.advertUrl)
                })

            }

        }

        holder.shareBtn.setOnClickListener {
            events.share(post)
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

}