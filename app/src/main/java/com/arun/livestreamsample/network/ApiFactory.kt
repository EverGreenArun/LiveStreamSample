package com.arun.livestreamsample.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit

object ApiFactory {
    private const val BASE_URL = "https://m.livestreamfails.com"

    fun makeRetrofitService(): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(Api::class.java)
    }
}