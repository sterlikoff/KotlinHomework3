package ru.sterlikoff.hw3.models.dto

data class PostInDto(

    val id: Int,
    val title: String,
    val userId: Int,
    val time: Long,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val rePostCount: Int = 0,
    val lon: Double? = null,
    val lat: Double? = null,
    val videoUrl: String? = null,
    val parentId: Int? = null,
    val advertUrl: String? = null,
    val imageId: String? = null

)