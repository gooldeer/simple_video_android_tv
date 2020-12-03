package io.moysa.videocheck.app.mappers

import io.moysa.videocheck.app.models.UiVideo
import io.moysa.videocheck.domain.model.Video

internal fun Video.toUiModel() : UiVideo {
    return UiVideo(this.name, this.url, this.snapshotUrl)
}