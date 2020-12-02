package io.moysa.videocheck.domain.repo

import io.moysa.videocheck.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface IVideosRepository {
    suspend fun loadVideos(forceRefresh: Boolean): Flow<List<Category>>
}