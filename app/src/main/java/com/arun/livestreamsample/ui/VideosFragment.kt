package com.arun.livestreamsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arun.livestreamsample.R
import com.arun.livestreamsample.base.BaseActivity
import com.arun.livestreamsample.base.BaseFragment
import com.arun.livestreamsample.base.BaseViewModel
import com.arun.livestreamsample.databinding.FragmentVideosBinding
import com.arun.livestreamsample.pojo.VideoFile
import com.arun.livestreamsample.ui.adapters.OnVideoClickListener
import com.arun.livestreamsample.ui.adapters.VideosAdapter
import com.arun.livestreamsample.utility.UiUtility

class VideosFragment : BaseFragment<FragmentVideosBinding, VideosFragmentViewModel>() {

    companion object {
        fun newInstance() = VideosFragment()
    }

    private val columnWidth = 400f
    private var isLoading: Boolean = false
    private lateinit var dataBinding: FragmentVideosBinding
    private lateinit var videosAdapter: VideosAdapter
    private val layoutManager by lazy {
        GridLayoutManager(activity, UiUtility.calculateNoOfColumns(activity?.applicationContext, columnWidth))
    }

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            activity?.application?.let { VideosFragmentViewModel.VideosFragmentViewModelFactory(it) })
            .get(VideosFragmentViewModel::class.java)
    }
    var onVideoSelectedCallBack: OnVideoSelectedCallBack? = null

    override fun setActionBarTitle() {
        (activity as BaseActivity<*>).updateTitle(getString(R.string.app_name))
        (activity as BaseActivity<*>).showActionBarBackButton(false)
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun getDataBinding(inflater: LayoutInflater, container: ViewGroup?): ViewDataBinding {
        dataBinding = FragmentVideosBinding.inflate(inflater, container, false)
        return dataBinding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.recyclerVideos.layoutManager = layoutManager
        videosAdapter = VideosAdapter(viewModel.videos, onVideoClickListener)
        dataBinding.recyclerVideos.adapter = videosAdapter
        setLoadMoreListener()
        loadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLiveDataObserver()
    }


    private fun loadData() {
        if (isNetworkConnected()) {
            isLoading = true
            videosAdapter.addLoader()
            viewModel.getPosters()
        }
    }

    private fun setLoadMoreListener() {
        dataBinding.recyclerVideos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val visibleThreshold = 5
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPOs = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItemPOs + visibleThreshold) {
                    loadData()
                }
            }
        })
    }

    private fun initLiveDataObserver() {
        val nameObserver = Observer<Status> {
            it?.let { status ->
                when (status) {
                    Status.SUCCESS, Status.FAILURE -> {
                        isLoading = false
                        videosAdapter.removeLoader()
                    }
                }
            }
        }
        viewModel.liveData.observe(this, nameObserver)
    }

    fun onOrientationChanged() {
        layoutManager.spanCount = UiUtility.calculateNoOfColumns(activity?.applicationContext, columnWidth)
    }

    private val onVideoClickListener = object : OnVideoClickListener {
        override fun onVideoClick(video: VideoFile) {
            video.dataContent?.let { onVideoSelectedCallBack?.onVideoSelected(it) }
        }
    }
}

interface OnVideoSelectedCallBack {
    fun onVideoSelected(url: String)
}