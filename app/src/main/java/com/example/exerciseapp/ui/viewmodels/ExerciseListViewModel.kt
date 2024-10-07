package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.data.repository.IWorkoutRepository
import com.example.exerciseapp.ui.views.ExerciseListDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseListViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: IWorkoutRepository,
    private  val exerciseRepository: IExerciseRepository
) : ViewModel() {
    private val workoutId: Int = checkNotNull(savedStateHandle[ExerciseListDestination.workoutIdArg])
    private val _exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
    private val _title = MutableStateFlow<String>("")
    private val _muscleGroupList = MutableStateFlow<List<MuscleGroup>>(emptyList())

    val muscleGroupList: StateFlow<List<MuscleGroup>> = _muscleGroupList
    val exerciseList: StateFlow<List<Exercise>> = _exerciseList
    val title: StateFlow<String> = _title

    init {
        loadExercises()
        getMuscleGroups()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val workoutWithExercises = workoutRepository.getWorkoutWithExercises(workoutId)
            _title.value = workoutWithExercises.workout.name
            _exerciseList.value = workoutWithExercises.exercises
        }
    }

    private fun getMuscleGroups() {
        viewModelScope.launch {
            _muscleGroupList.value = exerciseRepository.getAllMuscleGroups()
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exercise)
            loadExercises()
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.updateExercise(exercise)
            loadExercises()
        }
    }
}