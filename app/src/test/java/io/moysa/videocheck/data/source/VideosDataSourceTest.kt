package io.moysa.videocheck.data.source

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import io.moysa.videocheck.data.api.VideosApi
import io.moysa.videocheck.data.cache.VideosDao
import io.moysa.videocheck.data.models.entity.CategoryEntity
import io.moysa.videocheck.data.models.entity.CategoryWithVideosRelationEntity
import io.moysa.videocheck.data.models.entity.VideoEntity
import io.moysa.videocheck.data.models.response.CategoryResponse
import io.moysa.videocheck.data.models.response.VideoResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.mockito.Mockito

class VideosDataSourceTest {

    private lateinit var api : VideosApi
    private lateinit var dao : VideosDao

    private lateinit var dataSource : VideosDataSource

    @Before
    fun setUp() {
        api = Mockito.mock(VideosApi::class.java)
        dao = Mockito.mock(VideosDao::class.java)
        dataSource = VideosDataSource(api, dao)
    }

    @Test
    fun query_successNoCacheForce() = runBlocking {
        //when
        val forceRefresh : Boolean = true
        val response = listOf(
            CategoryResponse(
                "Category 1",
                listOf(VideoResponse("Video 1", "https://someurl.com", "snap"))
            )
        )
        dao.stub {
            onBlocking { isVideosCacheAvailable() }.doReturn(0)
        }
        api.stub {
            onBlocking { fetchVideos() }.doReturn(response)
        }

        //action
        val list = dataSource.query(forceRefresh).first()

        //then
        Mockito.verify(dao).clearDb()
        Mockito.verify(api).fetchVideos()
        Assert.assertEquals(1, list.size)
        Assert.assertEquals("Category 1", list[0].category.name)
    }

    @Test
    fun query_successCacheForce() = runBlocking {
        //when
        val forceRefresh : Boolean = true
        val response = listOf(
            CategoryResponse(
                "Category 1",
                listOf(VideoResponse("Video 1", "https://someurl.com", "snap"))
            )
        )
        dao.stub {
            onBlocking { isVideosCacheAvailable() }.doReturn(1)
        }
        api.stub {
            onBlocking { fetchVideos() }.doReturn(response)
        }

        //action
        val list = dataSource.query(forceRefresh).first()

        //then
        Mockito.verify(dao).clearDb()
        Mockito.verify(api).fetchVideos()
        Assert.assertEquals(1, list.size)
        Assert.assertEquals("Category 1", list[0].category.name)
    }

    @Test
    fun query_successCacheNoForce() = runBlocking {
        //when
        val forceRefresh : Boolean = false
        val videos = listOf(
            CategoryWithVideosRelationEntity(
                CategoryEntity(1, "Category 1"),
                listOf(VideoEntity("Video 1", "https://someurl.com", "snap", 1))
            )
        )
        dao.stub {
            onBlocking { isVideosCacheAvailable() }.doReturn(1)
        }
        dao.stub {
            onBlocking { allVideos() }.doReturn(videos)
        }

        //action
        val list = dataSource.query(forceRefresh).first()

        //then
        Mockito.verifyZeroInteractions(api)
        Assert.assertEquals(1, list.size)
        Assert.assertEquals("Category 1", list[0].category.name)
    }
}