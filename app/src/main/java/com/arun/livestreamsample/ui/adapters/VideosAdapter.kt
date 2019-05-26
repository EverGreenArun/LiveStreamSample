package com.arun.livestreamsample.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arun.livestreamsample.base.BaseViewHolder
import com.arun.livestreamsample.databinding.ViewHolderLoaderBinding
import com.arun.livestreamsample.databinding.ViewHolderVideoBinding
import com.arun.livestreamsample.pojo.VideoFile
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder

class VideosAdapter(
    private val videos: ArrayList<VideoFile>,
    private val onVideoClickListener: OnVideoClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        const val ITEM_TYPE_VIDEO = 0
        const val ITEM_TYPE_LOADER = 1
        const val LOADER = "loader"
    }

    private val loaderItem = VideoFile(LOADER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == ITEM_TYPE_VIDEO) {
            VideoViewHolder(
                ViewHolderVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onVideoClickListener
            )
        } else {
            VideoViewLoader(ViewHolderLoaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount() = videos.size

    override fun getItemViewType(position: Int): Int {
        return if (videos[position].id == LOADER) {
            ITEM_TYPE_LOADER
        } else {
            ITEM_TYPE_VIDEO
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is VideoViewHolder) {
            holder.onBind(videos[position])
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }

    fun addLoader() {
        videos.add(loaderItem)
        notifyDataSetChanged()
    }

    fun removeLoader() {
        videos.remove(loaderItem)
        notifyDataSetChanged()
    }
}

class VideoViewHolder(
    private val binding: ViewHolderVideoBinding,
    private val onVideoClickListener: OnVideoClickListener
) : BaseViewHolder(binding) {
    fun onBind(video: VideoFile) {
        binding.video = video
        video.dataThumbnail?.let {
            val imageRequest: ImageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(it))
                .build()

            Fresco.getImagePipeline().prefetchToBitmapCache(imageRequest, binding.iVThumbnail.context)

            binding.iVThumbnail.controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(binding.iVThumbnail.controller)
                .build()

        }
        binding.root.setOnClickListener { onVideoClickListener.onVideoClick(video) }
    }
}

class VideoViewLoader(binding: ViewHolderLoaderBinding) : BaseViewHolder(binding)

interface OnVideoClickListener {
    fun onVideoClick(video: VideoFile)
}