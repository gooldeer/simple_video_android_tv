package io.moysa.videocheck.app.ui.main

import java.util.Collections
import java.util.Timer
import java.util.TimerTask

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.leanback.app.BackgroundManager
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import io.moysa.videocheck.*
import io.moysa.videocheck.app.commons.Error
import io.moysa.videocheck.app.commons.Loading
import io.moysa.videocheck.app.commons.Success
import io.moysa.videocheck.app.commons.ViewModelFactory
import io.moysa.videocheck.app.mappers.toUiModel
import io.moysa.videocheck.app.ui.details.PlaybackActivity
import io.moysa.videocheck.app.ui.details.PlaybackVideoFragment
import io.moysa.videocheck.app.ui.details.PlaybackVideoFragment.Companion.VIDEO
import io.moysa.videocheck.app.ui.main.error.BrowseErrorActivity
import io.moysa.videocheck.domain.model.Category
import io.moysa.videocheck.domain.model.Video

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment() {

//    private val mHandler = Handler(Looper.getMainLooper())
//    private lateinit var mBackgroundManager: BackgroundManager
//    private var mDefaultBackground: Drawable? = null
//    private lateinit var mMetrics: DisplayMetrics
//    private var mBackgroundTimer: Timer? = null
//    private var mBackgroundUri: String? = null

    private lateinit var mViewModelFactory: ViewModelFactory
    private lateinit var mViewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

//        prepareBackgroundManager()
        setupUIElements()
        prepareViewModel()
        setupEventListeners()
    }

    private fun prepareViewModel() {
        mViewModelFactory = ViewModelFactory()
        mViewModel = ViewModelProvider(this, mViewModelFactory)
                .get(MainViewModel::class.java)

        mViewModel.uiState.observe(this, Observer {
            when (it) {
                is Loading -> displayLoadingState()
                is Success -> hideLoadingState()
                is Error -> displayErrorState(it.error)
            }
        })

        mViewModel.videoCategories.observe(this, Observer {
            displayResults(it)
        })

        mViewModel.executeVideosLoad(forceRefresh = false)
    }

    private fun displayResults(it: List<Category>?) {
        if (adapter is MainBrowseAdapter) {
            val a = adapter as MainBrowseAdapter;
            a.fillCategories(it ?: emptyList());
        }
    }

    private fun displayErrorState(error: Throwable) {
        //TODO display error
    }

    private fun hideLoadingState() {
        //TODO hide loading
    }

    private fun displayLoadingState() {
        //TODO display loading
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "onDestroy: " + mBackgroundTimer?.toString())
//        mBackgroundTimer?.cancel()
//    }

//    private fun prepareBackgroundManager() {
//
//        mBackgroundManager = BackgroundManager.getInstance(activity)
//        mBackgroundManager.attach(activity?.window)
//        mDefaultBackground = ContextCompat.getDrawable(context!!, R.drawable.default_background)
//        mMetrics = DisplayMetrics()
//        activity?.windowManager?.defaultDisplay?.getMetrics(mMetrics)
//    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(context!!, R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(context!!, R.color.search_opaque)

        adapter = MainBrowseAdapter()
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(context, "Implement your own in-app search", Toast.LENGTH_LONG)
                    .show()
        }

        onItemViewClickedListener = ItemViewClickedListener()
//        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
                itemViewHolder: Presenter.ViewHolder,
                item: Any,
                rowViewHolder: RowPresenter.ViewHolder,
                row: Row) {

            if (item is Video) {
                Log.d(TAG, "Item: " + item.toString())
                val intent = Intent(context, PlaybackActivity::class.java)
                intent.putExtra(VIDEO, item.toUiModel())

                activity!!.startActivity(intent)
            }
        }
    }

//    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
//        override fun onItemSelected(itemViewHolder: Presenter.ViewHolder?, item: Any?,
//                                    rowViewHolder: RowPresenter.ViewHolder, row: Row) {
//            if (item is Video) {
//                mBackgroundUri = item.snapshotUrl
//                startBackgroundTimer()
//            }
//        }
//    }
//
//    private fun updateBackground(uri: String?) {
//        val width = mMetrics.widthPixels
//        val height = mMetrics.heightPixels
//        Glide.with(context)
//                .load(uri)
//                .centerCrop()
//                .error(mDefaultBackground)
//                .into<SimpleTarget<GlideDrawable>>(
//                        object : SimpleTarget<GlideDrawable>(width, height) {
//                            override fun onResourceReady(resource: GlideDrawable,
//                                                         glideAnimation: GlideAnimation<in GlideDrawable>) {
//                                mBackgroundManager.drawable = resource
//                            }
//                        })
//        mBackgroundTimer?.cancel()
//    }
//
//    private fun startBackgroundTimer() {
//        mBackgroundTimer?.cancel()
//        mBackgroundTimer = Timer()
//        mBackgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
//    }
//
//    private inner class UpdateBackgroundTask : TimerTask() {
//
//        override fun run() {
//            mHandler.post { updateBackground(mBackgroundUri) }
//        }
//    }

    companion object {
        private val TAG = "MainFragment"

//        private val BACKGROUND_UPDATE_DELAY = 300
        private val NUM_ROWS = 6
        private val NUM_COLS = 15
    }
}