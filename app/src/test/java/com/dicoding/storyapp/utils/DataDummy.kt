package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.remote.response.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "Lorem Ipsum + $i",
                "Dummy Name + $i",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "Dummy Created At + $i",
                i.toString(),
                i.toString(),
            )
            items.add(story)
        }
        return items
    }
}