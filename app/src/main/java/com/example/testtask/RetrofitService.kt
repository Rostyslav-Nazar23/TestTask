package com.example.testtask

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {
    @GET("entities/getAllIds")
    suspend fun getDataObject(): Response<DataObject>

    @GET("object/{id}")
    suspend fun getContentObject(@Path("id") id: Int): Response<ContentObject>
}
