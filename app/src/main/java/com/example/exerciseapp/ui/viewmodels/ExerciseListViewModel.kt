package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.repository.ExerciseRepository
import com.example.exerciseapp.data.repository.WorkoutRepository
import com.example.exerciseapp.ui.views.ExerciseListDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseListViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
    private  val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val workoutId: Int = checkNotNull(savedStateHandle[ExerciseListDestination.workoutIdArg])
    private val _exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
    private val _title = MutableStateFlow<String>("")

    val exerciseList: StateFlow<List<Exercise>> = _exerciseList
    val title: StateFlow<String> = _title

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val workoutWithExercises = workoutRepository.getWorkoutWithExercises(workoutId)
            _title.value = workoutWithExercises.workout.name
            _exerciseList.value = workoutWithExercises.exercises
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exercise)
            loadExercises()
        }
    }

    fun renameExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.updateExercise(exercise)
            loadExercises()
        }
    }
}