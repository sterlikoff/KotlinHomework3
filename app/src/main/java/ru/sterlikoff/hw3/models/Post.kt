package ru.sterlikoff.hw3.models

import ru.sterlikoff.hw3.interfaces.Item

open class Post(

    val title: String,
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

): Item {

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

    private fun numericalWordForm(value: Long, form1: String, form2: String, form3: String): String {

        if (value in 10..20) return form3

        return when (value % 10) {

            1L -> form1
            in 2L..4L -> form2
            else -> form3
        }

    }

    private fun timeAgoString(timeAgo: Long): String {

        if (timeAgo < 60) return "менее минуты назад"

        if (timeAgo < 60 * 60) {

            val m: Long = timeAgo / 60
            return "$m ${numericalWordForm(m, "минуту", "минуты", "минут")} назад"

        }

        if (timeAgo < 60 * 60 * 24) {

            val h: Long = timeAgo / (60 * 60)
            return "$h ${numericalWordForm(h, "час", "часа", "часов")} назад"

        }

        if (timeAgo <= 60 * 60 * 24 * 365) {

            val d: Long = timeAgo / (60 * 60 * 24)
            return "$d ${numericalWordForm(d, "день", "дня", "дней")} назад"

        }

        return "больше года назад"

    }

    fun getAgoString(): String {

        return timeAgoString(System.currentTimeMillis() - time)

    }

}