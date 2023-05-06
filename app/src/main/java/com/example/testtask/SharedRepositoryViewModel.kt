package com.example.testtask

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SharedRepositoryViewModel: ViewModel() {
    private val repository = SharedRepository()

    private val _dataObjectLiveData = MutableLiveData<DataObject?>()
    val dataObjectLiveData: LiveData<DataObject?> = _dataObjectLiveData

    private val _contentObjectLiveData = MutableLiveData<ContentObject?>()
    val contentObjectLiveData: LiveData<ContentObject?> = _contentObjectLiveData

    fun refreshDataObject() {
        viewModelScope.launch {
            val response = repository.getDataObject()
            _dataObjectLiveData.postValue(response)
        }
    }

    fun refreshContentObject(id: Int) {
        viewModelScope.launch {
            val response = repository.getContentObject(id)
            _contentObjectLiveData.postValue(response)
        }
    }
}