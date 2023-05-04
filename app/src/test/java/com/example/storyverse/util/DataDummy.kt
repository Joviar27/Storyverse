package com.example.storyverse.util

import com.example.storyverse.domain.entity.StoryEntity

//The dummy data that simulates data from room
object DataDummy {
    fun generateDummyStoriesResponse() : List<StoryEntity>{
        val quotes : MutableList<StoryEntity> = arrayListOf()
        for(i in 0..100){
            val quote = StoryEntity(
                id = i.toString(),
                name = "name $i",
                description = "desc $i",
                photoUri = "uri $i"
            )
            quotes.add(quote)
        }
        return quotes
    }
}