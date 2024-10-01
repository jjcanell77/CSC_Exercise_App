package com.example.exerciseapp.data

import android.content.Context
import com.example.exerciseapp.data.repository.ExerciseRepository
import com.example.exerciseapp.data.repository.LogRepository
import com.example.exerciseapp.data.repository.WorkoutRepository

interface AppContainer {
    val exerciseRepository: ExerciseRepository
    val workoutRepository: WorkoutRepository
    val logRepository: LogRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val database: ExerciseAppDatabase by lazy {
        ExerciseAppDatabase.getDatabase(context)
    }

    override val exerciseRepository: ExerciseRepository by lazy {
        ExerciseRepository(database.exerciseDao())
    }

    override val workoutRepository: WorkoutRepository by lazy {
        WorkoutRepository(database.workoutDao(), database.workoutExerciseDao())
    }

    override val logRepository: LogRepository by lazy {
        LogRepository(database.logDao())
    }
}