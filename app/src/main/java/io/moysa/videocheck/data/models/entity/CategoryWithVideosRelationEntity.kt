package io.moysa.videocheck.data.models.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class CategoryWithVideosRelationEntity (
    @Embedded
    val category: CategoryEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "category_id"
    )
    val videos: List<VideoEntity>
)
