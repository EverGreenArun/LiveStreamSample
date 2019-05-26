package com.arun.livestreamsample.pojo

data class VideoFile(
    val id: String, val dataTitle: String? = null,
    val dataNsfw: String? = null, val dataScore: String? = null,
    val dataTime: String? = null, val dataStreamerName: String? = null,
    val dataGameName: String? = null, val dataThumbnail: String? = null,
    val dataContent: String? = null
)