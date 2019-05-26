package com.arun.livestreamsample.repo

import com.arun.livestreamsample.network.ApiFactory
import com.arun.livestreamsample.pojo.VideoFile
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Response

object PosterRemoteRepo {
    private val api = ApiFactory.makeRetrofitService()
    suspend fun getPosters(pageNo:String): ArrayList<VideoFile> {
        val result: Response<ResponseBody> = api.getVideosAsync(pageNo).await()
        val videoFiles = ArrayList<VideoFile>()
        if (result.isSuccessful) {
            result.body()?.string()?.let {
                val document = Jsoup.parse(it)
                val elements = document.select("div [class=card col-sm-3 post-card]")
                for (element in elements) {
                    videoFiles.add(
                        VideoFile(
                            element.attr("id"), element.attr("data-title"),
                            element.attr("data-nsfw"), element.attr("data-score"),
                            element.attr("data-time"), element.attr("data-streamer_name"),
                            element.attr("data-game_name"), element.attr("data-thumbnail"),
                            element.attr("data-content")
                        )
                    )
                }
            }
        }
        return videoFiles
    }
}