package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.ui.views.MuscleGroupScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MuscleGroupViewModel(
    private val exerciseRepository: IExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val muscleGroupId: Int = checkNotNull(savedStateHandle[MuscleGroupScreenDestination.muscleGroupIdArg])
    private val _exerciseList = MutableStateFlow<List<Exercise>>(emptyList())
    private val _title = MutableStateFlow<String>("")
    private val _muscleGroupList = MutableStateFlow<List<MuscleGroup>>(emptyList())

    val exerciseList: StateFlow<List<Exercise>> = _exerciseList
    val title: StateFlow<String> = _title
    val muscleGroupList: StateFlow<List<MuscleGroup>> = _muscleGroupList

    init {
        getExerciseByMuscleGroup()
        getMuscleGroups()
    }

    private fun getExerciseByMuscleGroup() {
        viewModelScope.launch {
            _exerciseList.value = exerciseRepository.getExercisesByMuscleGroup(muscleGroupId)
            _title.value = exerciseRepository.getMuscleGroupById(muscleGroupId).name
        }
    }

    private fun getMuscleGroups() {
        viewModelScope.launch {
            _muscleGroupList.value = exerciseRepository.getAllMuscleGroups()
        }
    }

    fun addExercise(name: String) {
        viewModelScope.launch {
            val newExercise = Exercise(
                name = name,
                typeId = muscleGroupId,
                isCustom = true
            )
            exerciseRepository.addExercise(newExercise)
            getExerciseByMuscleGroup()
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exercise)
            getExerciseByMuscleGroup()
        }
    }

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.updateExercise(exercise)
            getExerciseByMuscleGroup()
        }
    }
}
