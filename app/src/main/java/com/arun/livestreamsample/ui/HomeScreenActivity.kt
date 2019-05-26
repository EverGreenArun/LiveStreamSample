package com.arun.livestreamsample.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.arun.livestreamsample.R
import com.arun.livestreamsample.base.BaseActivity


class HomeScreenActivity : BaseActivity<ViewDataBinding>() {

    override fun getContentView(): Int = R.layout.activity_home_screen

    private val videosFragment by lazy { VideosFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videosFragment.onVideoSelectedCallBack = onVideoSelectedCallBack
        addFragment(R.id.container, videosFragment, VideosFragment::class.java.name)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videosFragment.onOrientationChanged()
        } else if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            videosFragment.onOrientationChanged()
        }
    }

    private val onVideoSelectedCallBack = object : OnVideoSelectedCallBack {
        override fun onVideoSelected(url: String) {
            val intent = Intent(this@HomeScreenActivity, VideoPlayerActivity::class.java)
            intent.putExtra(VideoPlayerActivity.ARG_VIDEO_URL, url)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager?.findFragmentById(R.id.container)
        if (fragment is VideosFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}