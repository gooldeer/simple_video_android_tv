package io.moysa.videocheck.data.mappers

import io.moysa.videocheck.data.models.entity.CategoryEntity
import io.moysa.videocheck.data.models.entity.CategoryWithVideosRelationEntity
import io.moysa.videocheck.data.models.entity.VideoEntity
import io.moysa.videocheck.domain.model.Category
import io.moysa.videocheck.domain.model.Video

internal fun CategoryWithVideosRelationEntity.toDomain() : Category {
    return Category(
        this.category.name,
        this.videos.map { it.toDomain() }
    )
}

internal fun VideoEntity.toDomain(): Video {
    return  Video(
        this.name,
        this.url,
        this.snapshotUrl
    )
}