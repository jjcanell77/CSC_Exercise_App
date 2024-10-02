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

    // Holds the Exercise being logged
    val exercise = MutableStateFlow<Exercise?>(null)

    // Holds the previous Log with its associated sets
    val previousLog = MutableStateFlow<LogWithSets?>(null)

    // Holds the current Log being created (without sets)
    val logDetails = MutableStateFlow(Log(exerciseId = 0, notes = ""))

    // Holds the list of ExerciseSets associated with the current Log
    val sets = MutableStateFlow<MutableList<ExerciseSet>>(mutableListOf())

    // State management for editing sets
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> get() = _isEditing

    // Index of the set currently being edited
    private val _currentSetIndex = MutableStateFlow(-1)

    // Input fields for weight and reps
    val weightInput = MutableStateFlow("")
    val repsInput = MutableStateFlow("")

    // Load the Exercise data
    fun loadExercise(exerciseId: Int) {
        viewModelScope.launch {
            val exerciseData = exerciseRepository.getExerciseById(exerciseId)
            exercise.value = exerciseData
            logDetails.value = logDetails.value.copy(exerciseId = exerciseId)
        }
    }

    // Load the previous Log for the Exercise
    fun loadPreviousLog(exerciseId: Int) {
        viewModelScope.launch {
            previousLog.value = logRepository.getLatestLogForExercise(exerciseId)
        }
    }

    // Handle changes to the weight input field
    fun onWeightChange(weight: String) {
        weightInput.value = weight
    }

    // Handle changes to the reps input field
    fun onRepsChange(reps: String) {
        repsInput.value = reps
    }

    // Handle changes to the notes input field
    fun onNotesChange(newNotes: String) {
        logDetails.value = logDetails.value.copy(notes = newNotes)
    }

    // Add or update the current set
    fun submitSet() {
        val weightValue = weightInput.value.toDoubleOrNull() ?: 0.0
        val repsValue = repsInput.value.toIntOrNull() ?: 0

        val newSet = ExerciseSet(
            logId = 0, // Will be set when the log is inserted
            weight = weightValue,
            reps = repsValue
        )

        val currentSets = sets.value.toMutableList()
        val setIndex = _currentSetIndex.value

        if (setIndex >= 0 && setIndex < currentSets.size) {
            // Update existing set
            currentSets[setIndex] = newSet
        } else {
            // Add new set
            currentSets.add(newSet)
        }

        sets.value = currentSets

        // Reset input fields and editing state
        weightInput.value = ""
        repsInput.value = ""
        _currentSetIndex.value = -1
        _isEditing.value = false
    }

    // Submit the Log along with its sets to the database
    fun submitLog() {
        viewModelScope.launch {
            val currentLog = logDetails.value

            // Insert the Log and get its generated ID
            val logId = logRepository.insertLog(currentLog)

            // Associate the sets with the new logId and insert them
            val setsToInsert = sets.value.map { it.copy(logId = logId.toInt()) }

            logRepository.insertExerciseSets(setsToInsert)

            // Reset the state after submission
            logDetails.value = logDetails.value.copy(notes = "")
            sets.value = mutableListOf()
            weightInput.value = ""
            repsInput.value = ""
            _currentSetIndex.value = -1
            _isEditing.value = false

            // Reload the previous log to reflect the new entry
            loadPreviousLog(currentLog.exerciseId)
        }
    }

    // Start editing a set at the given index
    fun editSet(index: Int) {
        val set = sets.value.getOrNull(index)
        if (set != null) {
            weightInput.value = set.weight.toString()
            repsInput.value = set.reps.toString()
            _currentSetIndex.value = index
            _isEditing.value = true
        }
    }

    // Delete a set from the current list
    fun deleteSet(index: Int) {
        val currentSets = sets.value.toMutableList()
        if (index >= 0 && index < currentSets.size) {
            currentSets.removeAt(index)
            sets.value = currentSets
        }
    }

    // Cancel editing mode
    fun cancelEdit() {
        weightInput.value = ""
        repsInput.value = ""
        _currentSetIndex.value = -1
        _isEditing.value = false
    }
}