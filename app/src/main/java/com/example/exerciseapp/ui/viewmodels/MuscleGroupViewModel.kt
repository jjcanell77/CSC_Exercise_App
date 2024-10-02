package com.example.exerciseapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MuscleGroupViewModel(
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _muscleGroupList = MutableStateFlow<List<MuscleGroup>>(emptyList())
    val muscleGroupList: StateFlow<List<MuscleGroup>> = _muscleGroupList
    val exerciseByMuscleGroup = MutableStateFlow<List<Exercise> >(emptyList())

    private val muscleGroupId: Int = savedStateHandle.get<Int>("muscleGroupId") ?: 0
    init {
        getMuscleGroups()
        getExerciseByMuscleGroup(muscleGroupId)
    }

    private fun getMuscleGroups() {
        viewModelScope.launch {
            Log.d("MuscleGroupViewModel", "Fetching muscle groups")
            _muscleGroupList.value = exerciseRepository.getAllMuscleGroups()
            Log.d("MuscleGroupViewModel", "Muscle groups fetched: ${_muscleGroupList.value.size}")
        }
    }

    private fun getExerciseByMuscleGroup(muscleGroupId: Int) {
        viewModelScope.launch {
            exerciseByMuscleGroup.value = exerciseRepository.getExercisesByMuscleGroup(muscleGroupId)
        }
    }
}
