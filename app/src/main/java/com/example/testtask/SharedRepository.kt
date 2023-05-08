package com.example.testtask

import retrofit2.Response

class SharedRepository {
    suspend fun getDataObject(): NewResponse<DataObject> {
        return safeCall {
            RetrofitClient.RetrofitClient.getDataObject() }
    }

    suspend fun getContentObject(id: Int): NewResponse<ContentObject> {
        return safeCall { RetrofitClient.RetrofitClient.getContentObject(id) }
    }

    private inline fun <T> safeCall(call: () -> Response<T>): NewResponse<T> {
        return try {
            NewResponse.success(call.invoke())
        } catch (e: Exception) {
            NewResponse.failure(e)
        }
    }
}