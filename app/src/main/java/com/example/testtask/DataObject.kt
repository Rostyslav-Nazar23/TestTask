package com.example.testtask

import com.google.gson.annotations.SerializedName

data class DataObject(
    @SerializedName("data")
    var data: ArrayList<IdObject> = arrayListOf()
)
