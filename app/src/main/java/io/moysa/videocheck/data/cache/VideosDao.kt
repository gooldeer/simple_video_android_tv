package io.moysa.videocheck.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.moysa.videocheck.data.models.entity.CategoryEntity
import io.moysa.videocheck.data.models.entity.CategoryWithVideosRelationEntity
import io.moysa.videocheck.data.models.entity.VideoEntity

@Dao
interface VideosDao {
    @Query("SELECT * FROM CategoryEntity")
    suspend fun allVideos(): List<CategoryWithVideosRelationEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVideos(videos: List<VideoEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCategories(categories: List<CategoryEntity>)

    suspend fun saveAllCategories(categories: List<CategoryWithVideosRelationEntity>) {
        val cat = categories.map { it.category }
        val videos = categories.map { it.videos }.flatten()

        saveCategories(cat)
        saveVideos(videos)
    }
}