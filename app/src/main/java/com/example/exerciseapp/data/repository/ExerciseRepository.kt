package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.ExerciseDao
import com.example.exerciseapp.data.dao.MuscleGroupDao
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup

class ExerciseRepository(
    private val exerciseDao: ExerciseDao,
    private val muscleGroupDao: MuscleGroupDao
) {

    suspend fun getAllExercises(): List<Exercise> {
        return exerciseDao.getAllExercises()
    }

    suspend fun getAllMuscleGroups(): List<MuscleGroup> {
        return muscleGroupDao.getAllMuscleGroups()
    }

    suspend fun getExercisesByMuscleGroup(typeId: Int): List<Exercise> {
        return exerciseDao.getExercisesByMuscleGroup(typeId)
    }

    suspend fun getExercisesByWorkout(workoutId: Int): List<Exercise> {
        return exerciseDao.getExercisesByWorkout(workoutId)    }

    suspend fun getExerciseById(id: Int): Exercise {
        return exerciseDao.getExerciseById(id)
    }

    suspend fun getMuscleGroupById(id: Int): MuscleGroup {
        return exerciseDao.getMuscleGroupById(id)
    }

    suspend fun searchExercises(query: String): List<Exercise> {
        return exerciseDao.searchExercises("%$query%")
    }
}
