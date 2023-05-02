package com.example.storyverse.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryEntity(
    val id : String,
    val name : String,
    val description : String,
    val photoUri : String
) : Parcelable