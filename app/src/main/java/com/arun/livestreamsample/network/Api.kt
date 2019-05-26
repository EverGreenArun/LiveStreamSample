package com.arun.livestreamsample.network

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/load/loadPosts.php?loadPostMode=standard&loadPostOrder=hot&loadPostTimeFrame=all&loadPostNSFW=0")
    fun getVideosAsync(@Query("loadPostPage")  loadPostPage :String): Deferred<Response<ResponseBody>>
}