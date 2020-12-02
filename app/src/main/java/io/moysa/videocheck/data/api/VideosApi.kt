package io.moysa.videocheck.data.api

import io.moysa.videocheck.data.models.response.CategoryResponse
import retrofit2.http.GET

interface VideosApi {
    @GET("videos")
    suspend fun fetchVideos() : List<CategoryResponse>
}