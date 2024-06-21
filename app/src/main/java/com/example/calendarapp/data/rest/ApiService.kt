package com.example.calendarapp.data.rest

import com.example.calendarapp.data.rest.request.CreateTaskRequest
import com.example.calendarapp.data.rest.request.DeleteTaskRequest
import com.example.calendarapp.data.rest.request.GetTasksRequest
import com.example.calendarapp.data.rest.response.BaseResponse
import com.example.calendarapp.data.rest.response.TaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/getCalendarTaskList")
    suspend fun getAllTasks(@Body getTasksRequest: GetTasksRequest): Response<TaskResponse>

    @POST("api/storeCalendarTask")
    suspend fun createTask(@Body createTaskRequest: CreateTaskRequest) : Response<BaseResponse>

    @POST("api/deleteCalendarTask")
    suspend fun deleteTask(@Body deleteTaskRequest: DeleteTaskRequest): Response<BaseResponse>
}