package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.ExerciseDao
import com.example.exerciseapp.data.dao.MuscleGroupDao
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup

class ExerciseRepository(
    private val exerciseDao: ExerciseDao,
    private val muscleGroupDao: MuscleGroupDao
) : IExerciseRepository {

    override suspend fun getAllExercises(): List<Exercise> {
        return exerciseDao.getAllExercises()
    }

    override suspend fun getAllMuscleGroups(): List<MuscleGroup> {
        return muscleGroupDao.getAllMuscleGroups()
    }

    override suspend fun getExercisesByMuscleGroup(typeId: Int): List<Exercise> {
        return exerciseDao.getExercisesByMuscleGroup(typeId)
    }

    override suspend fun getExerciseById(id: Int): Exercise {
        return exerciseDao.getExerciseById(id)
    }

    override suspend fun getMuscleGroupById(id: Int): MuscleGroup {
        return exerciseDao.getMuscleGroupById(id)
    }

    override suspend fun searchExercises(query: String): List<Exercise> {
        return exerciseDao.searchExercises("%$query%")
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise)
    }

    override suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise)
    }

    override suspend fun addExercise(exercise: Exercise): Long {
        return exerciseDao.addExercise(exercise)
    }
}