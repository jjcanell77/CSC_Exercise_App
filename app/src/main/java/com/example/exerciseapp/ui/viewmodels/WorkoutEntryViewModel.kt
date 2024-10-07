package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.data.repository.IWorkoutRepository
import com.example.exerciseapp.ui.views.WorkoutEntryScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: IWorkoutRepository,
    private val exerciseRepository: IExerciseRepository
) : ViewModel() {
    private val _name: String = checkNotNull(savedStateHandle[WorkoutEntryScreenDestination.workoutNameArg])
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())

    val allExercises: StateFlow<List<Exercise>> = _exercises
    val name: String = _name
    val workouts = MutableStateFlow<List<Workout>>(emptyList())

    init {
        getExercises()
    }

    private fun getExercises() {
        viewModelScope.launch {
            _exercises.value = exerciseRepository.getAllExercises()
        }
    }

    fun addWorkout(name: String, exercises: List<Exercise>) {
        viewModelScope.launch {
            val newWorkout = Workout(name = name, isCustom = true)
            val workoutId = workoutRepository.insertWorkout(newWorkout).toInt()
            exercises.forEach { exercise ->
                workoutRepository.insertWorkoutExerciseCrossRef(
                    WorkoutExercise(workoutId = workoutId, exerciseId = exercise.id)
                )
            }
        }
    }
}