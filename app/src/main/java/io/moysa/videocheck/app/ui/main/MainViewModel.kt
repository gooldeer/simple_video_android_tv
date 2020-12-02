package io.moysa.videocheck.app.ui.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.moysa.videocheck.app.commons.Loading
import io.moysa.videocheck.app.commons.Success
import io.moysa.videocheck.app.commons.UiStateViewModel
import io.moysa.videocheck.data.repo.VideosRepository
import io.moysa.videocheck.domain.model.Category
import io.moysa.videocheck.domain.repo.IVideosRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainViewModel constructor(
        private val videosRepo: IVideosRepository
): UiStateViewModel() {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var sortJob: Job? = null

    val videoCategories: LiveData<List<Category>> get() = _videoCategories

    private var _videoCategories: MutableLiveData<List<Category>> = MutableLiveData()

    fun executeVideosLoad(forceRefresh: Boolean) {
        _uiState.value = Loading
        viewModelScope.launch(handler) {
            videosRepo.loadVideos(forceRefresh).collect { results ->
                _videoCategories.value = results
            }
            _uiState.value = Success
        }
    }

    fun sortCategories(asc: Boolean) {
        if (_uiState.value == Success) {
            val list = _videoCategories.value
            _uiState.value = Loading
            sortJob?.cancel()
            sortJob = coroutineScope.launch {
                _videoCategories.value = sortList(asc, list)
                delay(1000)
                _uiState.value = Success
            }
        }
    }

    private fun sortList(
            asc: Boolean,
            list: List<Category>?
    ): List<Category>? {
        return if (asc)
            list?.sortedBy { it.name }
        else
            list?.sortedByDescending { it.name }
    }

    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        sortJob?.cancel();
    }
}