package io.moysa.videocheck.data.models.response

data class CategoryResponse (
    val name: String,
    val videos: List<VideoResponse>
)
