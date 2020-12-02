package io.moysa.videocheck.data.repo

import io.moysa.videocheck.data.mappers.toDomain
import io.moysa.videocheck.data.source.VideosDataSource
import io.moysa.videocheck.domain.model.Category
import io.moysa.videocheck.domain.repo.IVideosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VideosRepository constructor(
        private val dataSource: VideosDataSource
) : IVideosRepository {
    override suspend fun loadVideos(forceRefresh: Boolean): Flow<List<Category>> {
        return dataSource.query(forceRefresh).map { it.map { results -> results.toDomain() } }
    }
}