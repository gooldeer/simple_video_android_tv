package io.moysa.videocheck.data.source

import io.moysa.videocheck.data.api.VideosApi
import io.moysa.videocheck.data.cache.VideosDao
import io.moysa.videocheck.data.mappers.toEntity
import io.moysa.videocheck.data.models.entity.CategoryWithVideosRelationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideosDataSource constructor(
        private val api: VideosApi,
        private val dao: VideosDao
) {
    suspend fun query(forceRefresh: Boolean) : Flow<List<CategoryWithVideosRelationEntity>> {
        val isCacheAvailable = dao.isVideosCacheAvailable() > 0

        if (forceRefresh) {
            dao.clearDb()
        } else if (isCacheAvailable) {
            return flow { emit(dao.allVideos()) }
        }
        val entities = api
                .fetchVideos()
                .mapIndexed { ci, r ->  CategoryWithVideosRelationEntity(
                        r.toEntity(ci),
                        r.videos.mapIndexed { vi, v -> v.toEntity(vi, ci) }
                )}
        dao.saveAllCategories(entities)

        return flow { emit(entities) }
    }
}