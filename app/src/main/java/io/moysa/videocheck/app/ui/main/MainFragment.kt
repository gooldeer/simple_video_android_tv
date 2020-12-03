package io.moysa.videocheck.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.leanback.app.BrowseSupportFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import io.moysa.videocheck.*
import io.moysa.videocheck.app.commons.Error
import io.moysa.videocheck.app.commons.Loading
import io.moysa.videocheck.app.commons.Success
import io.moysa.videocheck.app.commons.ViewModelFactory
import io.moysa.videocheck.app.mappers.toUiModel
import io.moysa.videocheck.app.ui.details.PlaybackActivity
import io.moysa.videocheck.app.ui.details.PlaybackVideoFragment.Companion.VIDEO
import io.moysa.videocheck.app.ui.main.error.ErrorFragment
import io.moysa.videocheck.domain.model.Category
import io.moysa.videocheck.domain.model.Video

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment() {

    private lateinit var mViewModelFactory: ViewModelFactory
    private lateinit var mViewModel: MainViewModel

    private lateinit var mSpinnerFragment : SpinnerFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

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
        fragmentManager!!.beginTransaction().add(R.id.main_browse_fragment, ErrorFragment()).commit()
    }

    private fun hideLoadingState() {
        fragmentManager!!.beginTransaction().remove(mSpinnerFragment).commit()
    }

    private fun displayLoadingState() {
        mSpinnerFragment = SpinnerFragment()
        fragmentManager!!.beginTransaction().add(R.id.main_browse_fragment, mSpinnerFragment).commit()
    }

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

    class SpinnerFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val progressBar = ProgressBar(container?.context)
            if (container is FrameLayout) {
                val layoutParams = FrameLayout.LayoutParams(
                    SPINNER_WIDTH,
                    SPINNER_HEIGHT, Gravity.CENTER)
                progressBar.layoutParams = layoutParams
            }
            return progressBar
        }
    }

    companion object {
        private val TAG = "MainFragment"

        private val SPINNER_WIDTH = 100
        private val SPINNER_HEIGHT = 100
    }
}