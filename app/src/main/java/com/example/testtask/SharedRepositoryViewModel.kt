package com.example.testtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedRepositoryViewModel : ViewModel() {
    private val repository = SharedRepository()

    private val _dataObjectLiveData = MutableLiveData<DataObject?>()
    val dataObjectLiveData: LiveData<DataObject?> = _dataObjectLiveData

    private val _contentObjectLiveData = MutableLiveData<ContentObject?>()
    val contentObjectLiveData: LiveData<ContentObject?> = _contentObjectLiveData

    suspend fun pause(){
        delay(100)
    }

    fun refreshDataObject() {
        viewModelScope.launch {
            val response = repository.getDataObject()
            if (response.isSuccessful) {
                _dataObjectLiveData.postValue(response.body)
            }
            if (response.failed){
                _dataObjectLiveData.postValue(null)
            }
        }
    }

    fun refreshContentObject(id: Int) {
        viewModelScope.launch {
            val response = repository.getContentObject(id)
            if (response.isSuccessful){
                _contentObjectLiveData.postValue(response.body)
            }
            if (response.failed){
                _contentObjectLiveData.postValue(null)
            }
        }
    }
}