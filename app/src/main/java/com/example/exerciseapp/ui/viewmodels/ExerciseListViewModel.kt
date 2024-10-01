package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.repository.ExerciseRepository
import com.example.exerciseapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExerciseListViewModel(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val workout = MutableStateFlow<Workout?>(null)

    private val workoutId: Int = savedStateHandle.get<Int>("workoutId") ?: 0

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val workoutWithExercises = workoutRepository.getWorkoutWithExercises(workoutId)
            workout.value = workoutWithExercises.workout
            exercises.value = workoutWithExercises.exercises
        }
    }
}
