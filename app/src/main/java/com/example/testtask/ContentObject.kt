package com.example.testtask

import com.google.gson.annotations.SerializedName

data class ContentObject(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("url")
    val url: String? = null
)
