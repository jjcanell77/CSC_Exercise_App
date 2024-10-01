package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets
import com.example.exerciseapp.data.repository.ExerciseRepository
import com.example.exerciseapp.data.repository.LogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogViewModel(
    private val logRepository: LogRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    val exercise = MutableStateFlow<Exercise?>(null)
    val previousLog = MutableStateFlow<LogWithSets?>(null)
    val logDetails = MutableStateFlow(Log(exerciseId = 0, notes = ""))
    val sets = MutableStateFlow<MutableList<ExerciseSet>>(mutableListOf())

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> get() = _isEditing

    private val _currentSetIndex = MutableStateFlow(-1) // Index of the set being edited

    fun loadExercise(exerciseId: Int) {
        viewModelScope.launch {
            exercise.value = exerciseRepository.getExerciseById(exerciseId)
            logDetails.value = logDetails.value.copy(exerciseId = exerciseId)
        }
    }

    fun loadPreviousLog(exerciseId: Int) {
        viewModelScope.launch {
            previousLog.value = logRepository.getLatestLogForExercise(exerciseId)
        }
    }

    fun onSetDetailsChange(weight: String, reps: String) {
        val currentSets = sets.value
        val setIndex = _currentSetIndex.value

        val weightValue = weight.toDoubleOrNull() ?: 0.0
        val repsValue = reps.toIntOrNull() ?: 0

        val newSet = ExerciseSet(weight = weightValue, reps = repsValue)

        if (setIndex >= 0 && setIndex < currentSets.size) {
            // Update existing set
            currentSets[setIndex] = newSet
        } else {
            // Add new set
            currentSets.add(newSet)
        }

        sets.value = currentSets
    }

    fun onNotesChange(newNotes: String) {
        logDetails.value = logDetails.value.copy(notes = newNotes)
    }

    fun submitSet() {
        // Resets the input fields and exits edit mode
        _currentSetIndex.value = -1
        _isEditing.value = false
    }

    fun submitLog() {
        viewModelScope.launch {
            val currentLog = logDetails.value
            logRepository.insertLogWithSets(currentLog, sets.value)
            // Reset log details
            logDetails.value = logDetails.value.copy(notes = "")
            sets.value = mutableListOf()
            _currentSetIndex.value = -1
            _isEditing.value = false
            loadPreviousLog(currentLog.exerciseId) // Refresh previous log
        }
    }

    fun editSet(index: Int) {
        _currentSetIndex.value = index
        _isEditing.value = true
    }

    fun deleteSet(index: Int) {
        val currentSets = sets.value
        if (index >= 0 && index < currentSets.size) {
            currentSets.removeAt(index)
            sets.value = currentSets
        }
    }

    fun cancelEdit() {
        _currentSetIndex.value = -1
        _isEditing.value = false
    }
}
