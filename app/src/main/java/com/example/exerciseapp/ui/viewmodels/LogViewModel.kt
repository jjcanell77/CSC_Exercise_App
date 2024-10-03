package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets
import com.example.exerciseapp.data.repository.ExerciseRepository
import com.example.exerciseapp.data.repository.LogRepository
import com.example.exerciseapp.ui.views.LogEntryDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogViewModel(
    private val logRepository: LogRepository,
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    private val exerciseId: Int = checkNotNull(savedStateHandle[LogEntryDestination.exerciseIdArg])
    private val _uiState = MutableStateFlow(LogEntryUiState())

    val uiState: StateFlow<LogEntryUiState> get() = _uiState

    init {
        loadData(exerciseId)
    }

    private fun loadData(exerciseId: Int) {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseById(exerciseId)
            val previousLog = logRepository.getLatestLogForExercise(exerciseId)
            _uiState.value = _uiState.value.copy(
                exerciseName = exercise.name,
                previousLog = previousLog,
                logDetails = _uiState.value.logDetails.copy(exerciseId = exerciseId)
            )
        }
    }

    private fun loadPreviousLog(exerciseId: Int) {
        viewModelScope.launch {
            val previousLog = logRepository.getLatestLogForExercise(exerciseId)
            _uiState.value = _uiState.value.copy(previousLog = previousLog)
        }
    }

    fun onWeightChange(weight: String) {
        _uiState.value = _uiState.value.copy(weightInput = weight)
    }

    fun onRepsChange(reps: String) {
        _uiState.value = _uiState.value.copy(repsInput = reps)
    }

    fun onNotesChange(newNotes: String) {
        _uiState.value = _uiState.value.copy(
            logDetails = _uiState.value.logDetails.copy(notes = newNotes)
        )
    }

    fun submitSet() {
        val weightValue = _uiState.value.weightInput.toDoubleOrNull()
        val repsValue = _uiState.value.repsInput.toIntOrNull()

        if (weightValue == null || repsValue == null) {
            return
        }
        val newSet = ExerciseSet(
            logId = 0, // Will be set when the log is inserted
            weight = weightValue,
            reps = repsValue
        )
        val currentSets = _uiState.value.sets.toMutableList()
        val setIndex = _uiState.value.currentSetIndex
        if (setIndex >= 0 && setIndex < currentSets.size) {
            currentSets[setIndex] = newSet
        } else {
            currentSets.add(newSet)
        }
        _uiState.value = _uiState.value.copy(
            sets = currentSets,
            weightInput = "",
            repsInput = "",
            currentSetIndex = -1,
            isEditing = false
        )
    }

    fun submitLog() {
        viewModelScope.launch {
            try {
                val currentLog = _uiState.value.logDetails
                val logId = logRepository.insertLog(currentLog)
                val setsToInsert = _uiState.value.sets.map { it.copy(logId = logId.toInt()) }

                logRepository.insertExerciseSets(setsToInsert)

                _uiState.value = _uiState.value.copy(
                    logDetails = _uiState.value.logDetails.copy(notes = ""),
                    sets = emptyList(),
                    weightInput = "",
                    repsInput = "",
                    currentSetIndex = -1,
                    isEditing = false
                )

                loadPreviousLog(currentLog.exerciseId)
            } catch (e: Exception) {
                android.util.Log.e("LogViewModel", e.message.toString())
            }
        }
    }

    fun deleteSet(index: Int) {
        val currentSets = _uiState.value.sets.toMutableList()
        if (index >= 0 && index < currentSets.size) {
            currentSets.removeAt(index)
            _uiState.value = _uiState.value.copy(sets = currentSets)
        }
    }

    fun editSet(index: Int) {
        val set = _uiState.value.sets.getOrNull(index)
        if (set != null) {
            _uiState.value = _uiState.value.copy(
                weightInput = set.weight.toString(),
                repsInput = set.reps.toString(),
                currentSetIndex = index,
                isEditing = true
            )
        }
    }
}

data class LogEntryUiState(
    val exerciseName: String = "",
    val previousLog: LogWithSets? = null,
    val logDetails: Log = Log(exerciseId = 0, notes = ""),
    val sets: List<ExerciseSet> = emptyList(),
    val weightInput: String = "",
    val repsInput: String = "",
    val isEditing: Boolean = false,
    val currentSetIndex: Int = -1
)
