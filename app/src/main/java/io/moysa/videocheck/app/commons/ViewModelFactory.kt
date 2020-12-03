package io.moysa.videocheck.app.commons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.moysa.videocheck.app.VideoCheckApplication
import io.moysa.videocheck.app.ui.main.MainViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(VideoCheckApplication.instance.videosRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}