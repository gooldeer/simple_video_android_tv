package io.moysa.videocheck.data.models.response

data class CategoriesLoadResponse (
    val current: Int,
    val count: Int,
    val data: List<CategoryResponse>
)
