package com.admaja.storyappsubmission.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story_entity")
@Parcelize
data class StoryEntity (
        @field:SerializedName("photoUrl")
        val photoUrl: String,

        @field:SerializedName("createdAt")
        val createdAt: String,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("description")
        val description: String,

        @field:SerializedName("lon")
        val lon: Double,

        @PrimaryKey
        @field:SerializedName("id")
        val id: String,

        @field:SerializedName("lat")
        val lat: Double): Parcelable