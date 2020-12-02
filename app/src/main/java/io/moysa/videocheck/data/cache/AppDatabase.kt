package io.moysa.videocheck.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import io.moysa.videocheck.data.models.entity.CategoryEntity
import io.moysa.videocheck.data.models.entity.VideoEntity

@Database(entities = [CategoryEntity::class, VideoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videosDao() : VideosDao
}