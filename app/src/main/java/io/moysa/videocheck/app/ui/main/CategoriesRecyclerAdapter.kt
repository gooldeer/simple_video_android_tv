package io.moysa.videocheck.app.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.moysa.videocheck.R
import io.moysa.videocheck.app.commons.BaseViewHolder
import io.moysa.videocheck.domain.model.Category
import io.moysa.videocheck.domain.model.Video

class CategoriesRecyclerAdapter(


    //TODO refactor to act like card presenter for android tv specifically

    private val context: Context,
    val onClick: (Video) -> Unit) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var adapterDataList: List<Any> = emptyList()

    fun setItems(list: List<Category>) {
        adapterDataList = list.map { listOf(it, it.videos) }.flatten()
        notifyDataSetChanged();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_CATEGORY -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.category_item, parent, false)
                CategoryViewHolder(view)
            }
            TYPE_VIDEO -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.video_item, parent, false)
                VideoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return  adapterDataList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = adapterDataList[position]
        when (holder) {
            is CategoryViewHolder -> holder.bind(element as Category)
            is VideoViewHolder -> holder.bind(element as Video)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (adapterDataList[position]) {
            is Category -> TYPE_CATEGORY
            is Video -> TYPE_VIDEO
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    inner class CategoryViewHolder(itemView: View) : BaseViewHolder<Category>(itemView) {
        override fun bind(item: Category) {
            val name = itemView.findViewById<TextView>(R.id.category_name)
            val videos = itemView.findViewById<TextView>(R.id.category_videos)

            name.text = item.name
            //just lazy, I know how to use resources
            //TODO move to resources
            videos.text = "${item.videos.size} videos";
        }

    }

    inner class VideoViewHolder(itemView: View) : BaseViewHolder<Video>(itemView) {
        override fun bind(item: Video) {
            val name = itemView.findViewById<TextView>(R.id.video_name)

            name.text = item.name
            itemView.setOnClickListener { onClick(item) }
        }

    }

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_VIDEO = 1
    }
}