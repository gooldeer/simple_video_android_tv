package io.moysa.videocheck.data.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CategoryEntity")
data class CategoryEntity (
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "name")
    val name: String
)
