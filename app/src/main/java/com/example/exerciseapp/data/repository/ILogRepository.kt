package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets

interface ILogRepository {
    suspend fun insertLog(log: Log): Long
    suspend fun insertExerciseSets(sets: List<ExerciseSet>)
    suspend fun getLatestLogForExercise(exerciseId: Int): LogWithSets?
}