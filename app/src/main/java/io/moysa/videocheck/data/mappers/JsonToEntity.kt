package io.moysa.videocheck.data.mappers

import io.moysa.videocheck.data.models.entity.CategoryEntity
import io.moysa.videocheck.data.models.entity.VideoEntity
import io.moysa.videocheck.data.models.response.CategoryResponse
import io.moysa.videocheck.data.models.response.VideoResponse

internal fun VideoResponse.toEntity(categoryId: Int) : VideoEntity {
    return VideoEntity(
        this.name,
        this.url,
        this.snapshotUrl,
        categoryId
    )
}

internal fun CategoryResponse.toEntity(primaryKey: Int) : CategoryEntity {
    return  CategoryEntity(
        primaryKey,
        this.name
    )
}