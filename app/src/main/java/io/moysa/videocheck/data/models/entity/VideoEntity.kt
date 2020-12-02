package io.moysa.videocheck.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "VideoEntity")
data class VideoEntity (
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int
)
