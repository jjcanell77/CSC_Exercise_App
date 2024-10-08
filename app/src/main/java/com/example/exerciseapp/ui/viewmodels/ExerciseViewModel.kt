package com.example.exerciseapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.IExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val exerciseRepository: IExerciseRepository
) : ViewModel() {
    val searchText = MutableStateFlow("")
    val isSearching = MutableStateFlow(false)
    val exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
    val showList = MutableStateFlow(false)

    private val _muscleGroupList = MutableStateFlow<List<MuscleGroup>>(emptyList())
    val muscleGroupList: StateFlow<List<MuscleGroup>> = _muscleGroupList

    init {
        getMuscleGroups()
        getExercises()
    }

    private fun getMuscleGroups() {
        viewModelScope.launch {
            _muscleGroupList.value = exerciseRepository.getAllMuscleGroups()
        }
    }

    private fun getExercises() {
        viewModelScope.launch {
            exerciseList.value = exerciseRepository.getAllExercises()
        }
    }

    private fun searchExercises(query: String) {
        viewModelScope.launch {
            if (query.isNotEmpty()) {
                exerciseList.value = exerciseRepository.searchExercises(query)
                showList.value = true
            } else {
                getExercises()
                showList.value = false
            }
        }
    }

    fun getImageResourceId(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exercise)
            getExercises()
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.updateExercise(exercise)
            getExercises()
        }
    }

    fun onSearchTextChange(newText: String) {
        searchText.value = newText
        searchExercises(newText)
    }

    fun onToggleSearch() {
        isSearching.value = !isSearching.value
        if (!isSearching.value) {
            onCloseSearch()
        } else {
            showList.value = true
        }
    }

    fun onCloseSearch() {
        isSearching.value = false
        searchText.value = ""
        showList.value = false
        getExercises()
    }
}