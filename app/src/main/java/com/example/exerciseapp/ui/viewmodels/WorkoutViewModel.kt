package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    val workouts = MutableStateFlow<List<Workout>>(emptyList())

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            workouts.value = workoutRepository.getAllWorkouts()
        }
    }

//    fun addWorkout(workout: Workout) {TODO
//        viewModelScope.launch {
//            workoutRepository.insertWorkout(workout)
//            loadWorkouts()
//        }
//    }

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