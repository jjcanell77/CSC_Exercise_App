package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    // States
    val searchText = MutableStateFlow("")
    val isSearching = MutableStateFlow(false)
    val exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
    val showList = MutableStateFlow(false)
    val muscleGroupList = MutableStateFlow<List<MuscleGroup>>(emptyList())

    init {
        getMuscleGroups()
        getExercises()
    }

    private fun getMuscleGroups() {
        viewModelScope.launch {
            muscleGroupList.value = exerciseRepository.getAllMuscleGroups()
        }
    }

    fun getExercises() {
        viewModelScope.launch {
            exerciseList.value = exerciseRepository.getAllExercises()
        }
    }


    fun onSearchTextChange(newText: String) {
        searchText.value = newText
        searchExercises(newText)
    }

    fun onToggleSearch() {
        isSearching.value = !isSearching.value
    }

    fun onCloseSearch() {
        isSearching.value = false
        searchText.value = ""
        showList.value = false
    }

    private fun searchExercises(query: String) {
        viewModelScope.launch {
            exerciseList.value = exerciseRepository.searchExercises(query)
            showList.value = true
        }
    }
}
