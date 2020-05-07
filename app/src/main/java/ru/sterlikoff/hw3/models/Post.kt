package ru.sterlikoff.hw3.models

import android.content.Context
import ru.sterlikoff.hw3.R
import ru.sterlikoff.hw3.interfaces.Item
import ru.sterlikoff.hw3.models.dto.PostInDto

open class Post(

    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    private val time: Int,
    var likeCount: Int = 0,
    val commentCount: Int = 0,
    val rePostCount: Int = 0,
    val lon: Double? = null,
    val lat: Double? = null,
    val videoUrl: String? = null,
    val parent: Post? = null,
    val advertUrl: String? = null

) : Item {

    private var liked: Boolean = false

    fun like() {

        if (liked) {
            likeCount--
        } else {
            likeCount++
        }

        liked = !liked

    }

    fun isLiked() = liked

    fun getAgoString(context: Context): String {

        val timeAgo = (System.currentTimeMillis() - time)

        if (timeAgo < 60) return context.getString(R.string.less_then_minute_ago_label)

        if (timeAgo < 60 * 60) {

            val m: Long = timeAgo / 60
            val form = context.resources.getQuantityString(R.plurals.minute_plural, m.toInt())

            return "$m $form ${context.getString(R.string.back_label)}"

        }

        if (timeAgo < 60 * 60 * 24) {

            val h: Long = timeAgo / (60 * 60)
            val form = context.resources.getQuantityString(R.plurals.hour_plural, h.toInt())

            return "$h $form ${context.getString(R.string.back_label)}"

        }

        if (timeAgo <= 60 * 60 * 24 * 365) {

            val d: Long = timeAgo / (60 * 60 * 24)
            val form = context.resources.getQuantityString(R.plurals.day_plural, d.toInt())

            return "$d $form ${context.getString(R.string.back_label)}"

        }

        return context.getString(R.string.more_hour_ago_label)

    }

    companion object {

        fun fromInDto(input: PostInDto): Post {

            return Post(
                id = input.id,
                title = input.title,
                content = input.content,
                author = input.username,
                time = input.time.toInt(),
                likeCount = input.likeCount,
                commentCount = input.commentCount,
                rePostCount = input.rePostCount,
                lon = input.lon,
                lat = input.lat,
                videoUrl = input.videoUrl,
                parent = null, // @TODO
                advertUrl = input.advertUrl
            )

        }

    }

}