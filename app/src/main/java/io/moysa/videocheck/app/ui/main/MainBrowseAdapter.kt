package io.moysa.videocheck.app.ui.main

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import io.moysa.videocheck.domain.model.Category

class MainBrowseAdapter constructor(
        private var categories : List<Category> = emptyList()
) : ArrayObjectAdapter(ListRowPresenter()) {

    fun fillCategories(loaded: List<Category>) {
        clear()
        categories = loaded

        val cardPresenter = CardPresenter()
        categories.forEachIndexed { index, category ->

            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            category.videos.forEach { video -> listRowAdapter.add(video) }

            val header = HeaderItem(index.toLong(), category.name)
            add(ListRow(header, listRowAdapter))
        }
    }
}