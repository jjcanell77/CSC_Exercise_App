package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.repository.IWorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: IWorkoutRepository
) : ViewModel() {
    val workouts = MutableStateFlow<List<Workout>>(emptyList())

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            workouts.value = workoutRepository.getCustomWorkouts()
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workout)
            loadWorkouts()
        }
    }

    fun renameWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.updateWorkout(workout)
            loadWorkouts()
        }
    }
}