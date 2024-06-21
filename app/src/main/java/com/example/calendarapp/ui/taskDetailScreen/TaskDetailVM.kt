package com.example.calendarapp.ui.taskDetailScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.data.rest.ApiService
import com.example.calendarapp.data.rest.request.DeleteTaskRequest
import com.example.calendarapp.data.rest.response.BaseResponse
import com.example.calendarapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailVM @Inject constructor(private val apiService: ApiService) : ViewModel() {
    private val _deleteStatus = MutableLiveData<Result<BaseResponse>>()
    var title: String? = null
    var description: String? = null
    var date: String? = null
    var taskId: Int? = null

    fun initData(title: String?, description: String?, date: String?, taskId: Int?) {
        this.title = title
        this.description = description
        this.date = date
        this.taskId = taskId
    }

    fun deleteTask(deleteTaskRequest: DeleteTaskRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.deleteTask(deleteTaskRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _deleteStatus.postValue(Result.Success(it))
                    }
                }
                else {
                    _deleteStatus.postValue(Result.Error(response.message()))
                }
            } catch (e : Exception) {
                _deleteStatus.postValue(Result.RestError("Something Went Wrong"))
            }
        }

    }

    val isLoading = _deleteStatus.map {
        it is Result.Loading
    }

    val deleteStatus = _deleteStatus.map {
        if (it is Result.Success)
            it.data.status
        else null
    }

    val isError = _deleteStatus.map {
        when (it) {
            is Result.Error -> it.error
            is Result.RestError -> it.error
            else -> null
        }
    }
}