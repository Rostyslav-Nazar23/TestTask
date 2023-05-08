package com.example.testtask

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val baseUrl = "http://demo3005513.mockable.io/api/v1/"
    private fun getLoggingHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        return builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getLoggingHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val RetrofitClient: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}