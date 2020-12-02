package io.moysa.videocheck.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import io.moysa.videocheck.data.api.HttpClient
import io.moysa.videocheck.data.api.LoggingInterceptor
import io.moysa.videocheck.data.api.VideosApi
import io.moysa.videocheck.data.cache.AppDatabase
import io.moysa.videocheck.data.repo.VideosRepository
import io.moysa.videocheck.data.source.VideosDataSource
import io.moysa.videocheck.domain.repo.IVideosRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

open class VideoCheckApplication: Application() {

    private lateinit var videosRepo: IVideosRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        //Better for everything to be injected of course, will be redone with Koin
        //TODO inject with Koin
        initDatabase()
        initApi()

        videosRepo = VideosRepository(VideosDataSource(
                api = retrofit.create(VideosApi::class.java),
                dao = appDatabase.videosDao()
        ))
    }

    private fun initApi() {
        retrofit = Retrofit.Builder()
                .baseUrl("https://some_base_url.com/")
                .client(HttpClient.setupOkhttpClient(LoggingInterceptor.create()))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    private fun initDatabase() {
        appDatabase = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "video_check_db.db"
        ).fallbackToDestructiveMigration().build()
    }
}