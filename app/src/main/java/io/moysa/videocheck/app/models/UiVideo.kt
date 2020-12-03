package io.moysa.videocheck.app.models

import java.io.Serializable

data class UiVideo (
        val name : String,
        val url: String,
        val snapshotUrl: String? = null
) : Serializable {
}