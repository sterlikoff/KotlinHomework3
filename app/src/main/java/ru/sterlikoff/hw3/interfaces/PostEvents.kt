package ru.sterlikoff.hw3.interfaces

import ru.sterlikoff.hw3.models.Post

interface PostEvents {

    fun share(post: Post)
    fun next()

}