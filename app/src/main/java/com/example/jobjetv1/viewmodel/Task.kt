package com.example.jobjetv1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

// Data class to represent a single task
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val time: LocalTime,
    val date: LocalDate,
    val isCompleted: Boolean = false
)

// Enum for the status of the task
enum class TaskStatus {
    IN_PROGRESS, PENDING
}

// The ViewModel to hold the state and business logic for the task list screen
class TaskViewModel : ViewModel() {

    // Private MutableStateFlow to hold the list of tasks. This is internal to the ViewModel.
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    // Publicly exposed StateFlow that is read-only for the UI.
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        // Load initial dummy data when the ViewModel is created
        loadTasks()
    }

    private fun loadTasks() {
        // Use viewModelScope to launch a coroutine to simulate fetching data
        viewModelScope.launch {
            _tasks.value = listOf(
                Task(
                    id = 1,
                    title = "Complete Android Project",
                    description = "Finish the UI, integrate API, and write documentation",
                    status = TaskStatus.IN_PROGRESS,
                    time = LocalTime.of(14, 0),
                    date = LocalDate.of(2025, 3, 26),
                    isCompleted = true
                ),
                Task(
                    id = 2,
                    title = "Doctor Appointment 2",
                    description = "This task is related to Work. It needs to be completed",
                    status = TaskStatus.PENDING,
                    time = LocalTime.of(14, 0),
                    date = LocalDate.of(2025, 3, 26),
                    isCompleted = true
                ),
                Task(
                    id = 3,
                    title = "Meeting",
                    description = "This task is related to Fitness. It needs to be completed",
                    status = TaskStatus.PENDING,
                    time = LocalTime.of(14, 0),
                    date = LocalDate.of(2025, 3, 26),
                    isCompleted = false
                )
            )
        }
    }

    // Function to update the completion status of a task
    fun toggleTaskCompletion(taskId: Int) {
        val currentTasks = _tasks.value.toMutableList()
        val taskIndex = currentTasks.indexOfFirst { it.id == taskId }
        if (taskIndex != -1) {
            val task = currentTasks[taskIndex]
            currentTasks[taskIndex] = task.copy(isCompleted = !task.isCompleted)
            _tasks.value = currentTasks
        }
    }
}

