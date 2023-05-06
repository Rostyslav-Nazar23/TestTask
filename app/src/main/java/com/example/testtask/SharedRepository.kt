package com.example.testtask

class SharedRepository {
    suspend fun getDataObject(): DataObject? {
        val request = RetrofitClient.RetrofitClient.getDataObject()

        if (request.isSuccessful){
            return request.body()!!
        }
        return null
    }

    suspend fun getContentObject(id: Int): ContentObject?{
        val request = RetrofitClient.RetrofitClient.getContentObject(id)

        if (request.isSuccessful){
            return request.body()!!
        }
        return null
    }
}