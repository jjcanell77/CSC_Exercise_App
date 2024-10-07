package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup

interface IExerciseRepository {
    suspend fun getAllExercises(): List<Exercise>
    suspend fun getAllMuscleGroups(): List<MuscleGroup>
    suspend fun getExercisesByMuscleGroup(typeId: Int): List<Exercise>
    suspend fun getExerciseById(id: Int): Exercise
    suspend fun getMuscleGroupById(id: Int): MuscleGroup
    suspend fun searchExercises(query: String): List<Exercise>
    suspend fun deleteExercise(exercise: Exercise)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun addExercise(exercise: Exercise): Long
}