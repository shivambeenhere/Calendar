package com.example.calendarapp.ui.addTaskScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.data.rest.ApiService
import com.example.calendarapp.data.rest.request.CreateTaskRequest
import com.example.calendarapp.data.rest.response.BaseResponse
import com.example.calendarapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskVM @Inject constructor(private val apiService: ApiService) : ViewModel() {
    private val _addStatus = MutableLiveData<Result<BaseResponse>>()

    fun addTask(createTaskRequest: CreateTaskRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.createTask(createTaskRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _addStatus.postValue(Result.Success(it))
                    }
                }
                else {
                    _addStatus.postValue(Result.Error(response.message()))
                }
            } catch (e : Exception) {
                _addStatus.postValue(Result.RestError("Something Went Wrong"))
            }
        }
    }

    val isLoading = _addStatus.map {
        it is Result.Loading
    }

    val addStatus = _addStatus.map {
        if (it is Result.Success)
            it.data.status
        else null
    }

    val isError = _addStatus.map {
        when (it) {
            is Result.Error -> it.error
            is Result.RestError -> it.error
            else -> null
        }
    }
}