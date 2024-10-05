package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.repository.ExerciseRepository
import com.example.exerciseapp.data.repository.WorkoutRepository
import com.example.exerciseapp.ui.views.WorkoutEntryScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
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

    fun addExercise(name: String, typeId: Int) {
        viewModelScope.launch {
            val newExercise = Exercise(
                name = name,
                typeId = typeId,
                isCustom = true
            )
            val workoutId = exerciseRepository.addExercise(newExercise).toInt()
            getExercises()
        }
    }
}