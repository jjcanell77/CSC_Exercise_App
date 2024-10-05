package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.WorkoutDao
import com.example.exerciseapp.data.dao.WorkoutExerciseDao
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.model.WorkoutWithExercises

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao
) {
    suspend fun insertWorkout(workout: Workout): Long {
        return workoutDao.insertWorkout(workout)
    }

    suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExercise) {
        workoutExerciseDao.insertWorkoutExercise(crossRef)
    }

    suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises {
        return workoutExerciseDao.getWorkoutWithExercises(workoutId)
    }

    suspend fun getNonCustomWorkouts(): List<Workout> {
        return workoutDao.getWorkoutsByCustomFlag(isCustom = false)
    }

    suspend fun getCustomWorkouts(): List<Workout> {
        return workoutDao.getWorkoutsByCustomFlag(isCustom = true)
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun updateWorkout(workout: Workout) {
        workoutDao.updateWorkout(workout)
    }
}
