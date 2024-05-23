package com.dicoding.storyapp.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse (
    @field:SerializedName("error")
    var error: String,

    @field:SerializedName("message")
    var message: String,

    @field:SerializedName("listStory")
    var listStory: List<Story>
)

data class CreateStory(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

@Entity(tableName = "story")
@Parcelize
data class Story (

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: String? = null,

    @field:SerializedName("lon")
    val lon: String? = null,
) : Parcelable