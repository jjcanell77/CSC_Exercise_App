package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.repository.WorkoutRepository
import com.example.exerciseapp.ui.views.WorkoutEntryScreenDestination
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
            workouts.value = workoutRepository.getCustomWorkouts()
        }
    }

//    fun addWorkout(name: String, exercises: List<Exercise>) {
//        viewModelScope.launch {
//            val newWorkout = Workout(name = name, isCustom = true)
//            val workoutId = workoutRepository.insertWorkout(newWorkout).toInt()
//            // Add the exercises to the workout
//            exercises.forEach { exercise ->
//                workoutRepository.insertWorkoutExerciseCrossRef(
//                    WorkoutExercise(workoutId = workoutId.toInt(), exerciseId = exercise.id)
//                )
//            }
//            loadWorkouts() // Refresh the list
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