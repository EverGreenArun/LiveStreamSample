package com.arun.livestreamsample.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arun.livestreamsample.base.BaseViewModel
import com.arun.livestreamsample.pojo.VideoFile
import com.arun.livestreamsample.repo.PosterRemoteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VideosFragmentViewModel(application: Application) : BaseViewModel(application) {
    val liveData: MutableLiveData<Status> by lazy {
        MutableLiveData<Status>()
    }
    val videos = ArrayList<VideoFile>()

    private var pageCount = 0

    fun getPosters() {
        uiScope.launch {
            val list = getPostersFromRemoteStorage()
            list?.let {
                videos.addAll(it)
                pageCount += 1
                liveData.value = Status.SUCCESS
            }
            if (list == null) {
                liveData.value = Status.FAILURE
            }
        }
    }

    private suspend fun getPostersFromRemoteStorage(): ArrayList<VideoFile>? = withContext(Dispatchers.IO) {
        return@withContext PosterRemoteRepo.getPosters(pageCount.toString())
    }

    class VideosFragmentViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Application::class.java).newInstance(application)
        }
    }
}

enum class Status {
    SUCCESS, FAILURE
}