package com.example.exerciseapp.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.exerciseapp.ExerciseApplication
import com.example.exerciseapp.ui.viewmodels.ExerciseListViewModel
import com.example.exerciseapp.ui.viewmodels.ExerciseViewModel
import com.example.exerciseapp.ui.viewmodels.LogViewModel
import com.example.exerciseapp.ui.viewmodels.ProgramViewModel
import com.example.exerciseapp.ui.viewmodels.WorkoutViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExerciseViewModel(
                exerciseRepository = myApplication().container.exerciseRepository
            )
        }
        initializer {
            WorkoutViewModel(
                workoutRepository = myApplication().container.workoutRepository
            )
        }
        initializer {
            ProgramViewModel(
                workoutRepository = myApplication().container.workoutRepository
            )
        }
        initializer {
            ExerciseListViewModel(
                workoutRepository = myApplication().container.workoutRepository,
                exerciseRepository = myApplication().container.exerciseRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer {
            LogViewModel(
                logRepository = myApplication().container.logRepository,
                exerciseRepository = myApplication().container.exerciseRepository
            )
        }
        // Add other ViewModels as needed
    }
}


fun CreationExtras.myApplication(): ExerciseApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ExerciseApplication