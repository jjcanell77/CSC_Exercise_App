package com.example.exerciseapp.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    val searchText = MutableStateFlow("")
    val isSearching = MutableStateFlow(false)
    val exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
    val showList = MutableStateFlow(false)

    private val _muscleGroupList = MutableStateFlow<List<MuscleGroup>>(emptyList())
    val muscleGroupList: StateFlow<List<MuscleGroup>> = _muscleGroupList

    init {
        getMuscleGroups()
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

    fun getImageResourceId(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exercise)
            getExercises()
        }
    }

    fun updateExercise(name: String, typeId: Int) {
        viewModelScope.launch {
            val updatedExercise = Exercise(
                name = name,
                typeId = typeId,
                isCustom = true
            )
            exerciseRepository.updateExercise(updatedExercise)
            getExercises()
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
