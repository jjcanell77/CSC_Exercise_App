package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.ExerciseDao
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    suspend fun getAllMuscleGroups(): List<MuscleGroup> {
        return exerciseDao.getAllMuscleGroups()
    }

    suspend fun getExercisesByMuscleGroup(typeId: Int): List<Exercise> {
        return exerciseDao.getExercisesByMuscleGroup(typeId)
    }

    suspend fun getExerciseById(id: Int): Exercise {
        return exerciseDao.getExerciseById(id)
    }

    suspend fun searchExercises(query: String): List<Exercise> {
        return exerciseDao.searchExercises("%$query%")
    }
}
