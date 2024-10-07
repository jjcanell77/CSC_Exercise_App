package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.repository.IWorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgramViewModel(
    private val workoutRepository: IWorkoutRepository
) : ViewModel() {

    private val _programs = MutableStateFlow<List<Workout>>(emptyList())
    val programList: StateFlow<List<Workout>> = _programs

    init {
        loadPrograms()
    }

    private fun loadPrograms() {
        viewModelScope.launch {
            _programs.value = workoutRepository.getNonCustomWorkouts()
        }
    }
}
