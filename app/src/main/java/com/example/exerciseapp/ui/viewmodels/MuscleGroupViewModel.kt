package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.ExerciseRepository
import com.example.exerciseapp.ui.views.MuscleGroupScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MuscleGroupViewModel(
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val muscleGroupId: Int = checkNotNull(savedStateHandle[MuscleGroupScreenDestination.muscleGroupIdArg])
    private val _exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
    private val _title = MutableStateFlow<String>("")

    val exerciseList: StateFlow<List<Exercise>> = _exerciseList
    val title: StateFlow<String> = _title

    init {
        getExerciseByMuscleGroup()
    }

    private fun getExerciseByMuscleGroup() {
        viewModelScope.launch {
            _exerciseList.value = exerciseRepository.getExercisesByMuscleGroup(muscleGroupId)
            _title.value = exerciseRepository.getMuscleGroupById(muscleGroupId).name
        }
    }
}
