package com.example.storyverse.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val id : String,

    @field:ColumnInfo(name = "name")
    val name : String,

    @field:ColumnInfo(name = "desc")
    val description : String,

    @field:ColumnInfo(name = "photo")
    val photoUri : String,

    @field:ColumnInfo(name = "lat")
    val lat : Double = 0.0,

    @field:ColumnInfo(name = "lon")
    val lon : Double = 0.0
) : Parcelable