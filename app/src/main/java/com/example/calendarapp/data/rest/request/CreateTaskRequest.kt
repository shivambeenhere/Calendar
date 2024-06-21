package com.example.calendarapp.data.rest.request

import com.example.calendarapp.data.rest.response.TaskDetail

data class CreateTaskRequest(
    val user_id: Int,
    val task: TaskDetail
)
