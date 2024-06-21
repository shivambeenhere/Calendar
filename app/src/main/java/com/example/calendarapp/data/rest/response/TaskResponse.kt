package com.example.calendarapp.data.rest.response

data class TaskResponse(
    val tasks: List<TaskData>
)

data class TaskData(
    val task_id: Int,
    val task_detail: TaskDetail
)

data class TaskDetail(
    val title: String,
    val description: String,
    val created_date: String
)
