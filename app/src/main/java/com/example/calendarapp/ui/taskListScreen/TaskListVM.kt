package com.example.calendarapp.ui.taskListScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.data.rest.ApiService
import com.example.calendarapp.data.rest.request.GetTasksRequest
import com.example.calendarapp.data.rest.response.TaskResponse
import com.example.calendarapp.utils.Consts
import com.example.calendarapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListVM @Inject constructor(private val apiService: ApiService): ViewModel() {
    private val _tasks = MutableLiveData<Result<TaskResponse>>()

    fun fetchTasks() {
        _tasks.postValue(Result.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getAllTasks(GetTasksRequest(Consts.USER_ID))
                if (response.isSuccessful) {
                    response.body()?.let {
                        _tasks.postValue(Result.Success(it))
                    }
                }
                else {
                    _tasks.postValue(Result.Error(response.message()))
                }
            } catch (e : Exception) {
                _tasks.postValue(Result.RestError("Something Went Wrong"))
            }
        }
    }

    val isLoading = _tasks.map {
        it is Result.Loading
    }

    val taskData = _tasks.map {
        if (it is Result.Success)
            it.data.tasks
        else null
    }

    val isError = _tasks.map {
        when (it) {
            is Result.Error -> it.error
            is Result.RestError -> it.error
            else -> null
        }
    }
}