package com.example.exerciseapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LogViewModel : ViewModel() {
    var logUiState by mutableStateOf(TaskUiState())
        private set

    private val exerciseId: Int = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])

    init {
        viewModelScope.launch {
            logUiState = toDoItemsRepository.getTaskStream(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(true)
        }
    }

    suspend fun deleteItem() {
        toDoItemsRepository.deleteTask(logUiState.taskDetails.toTask())
    }


    suspend fun updateTask() {
        if (validateInput(taskUiState.taskDetails)) {
            toDoItemsRepository.updateTask(logUiState.taskDetails.toTask())
        }
    }

    fun updateUiState(taskDetails: TaskDetails) {
        logUiState =
            TaskUiState(
                taskDetails = taskDetails,
                isEntryValid = validateInput(taskDetails))
    }

    private fun validateInput(uiState: TaskDetails = logUiState.taskDetails): Boolean {
        return with(uiState) {
            task.isNotBlank()
        }
    }
}