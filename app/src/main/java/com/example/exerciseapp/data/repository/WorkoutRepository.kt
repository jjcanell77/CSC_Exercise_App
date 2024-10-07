package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.WorkoutDao
import com.example.exerciseapp.data.dao.WorkoutExerciseDao
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.model.WorkoutWithExercises

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao
) : IWorkoutRepository {
    override suspend fun insertWorkout(workout: Workout): Long {
        return workoutDao.insertWorkout(workout)
    }

    override suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExercise) {
        workoutExerciseDao.insertWorkoutExercise(crossRef)
    }

    override suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises {
        return workoutExerciseDao.getWorkoutWithExercises(workoutId)
    }

    override suspend fun getNonCustomWorkouts(): List<Workout> {
        return workoutDao.getWorkoutsByCustomFlag(isCustom = false)
    }

    override suspend fun getCustomWorkouts(): List<Workout> {
        return workoutDao.getWorkoutsByCustomFlag(isCustom = true)
    }

    override suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }

    override suspend fun updateWorkout(workout: Workout) {
        workoutDao.updateWorkout(workout)
    }
}