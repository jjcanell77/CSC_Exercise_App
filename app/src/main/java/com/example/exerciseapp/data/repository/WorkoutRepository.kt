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

    suspend fun getAllWorkouts(): List<Workout> {
        return workoutDao.getAllWorkouts()
    }

    suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises {
        return workoutDao.getWorkoutWithExercises(workoutId)
    }

    suspend fun insertWorkoutWithExercises(workout: Workout, exerciseIds: List<Int>) {
        val workoutId = workoutDao.insertWorkout(workout).toInt()
        exerciseIds.forEach { exerciseId ->
            workoutExerciseDao.insertWorkoutExercise(
                WorkoutExercise(workoutId = workoutId, exerciseId = exerciseId)
            )
        }
    }

    suspend fun getNonCustomWorkouts(): List<Workout> {
        return workoutDao.getWorkoutsByCustomFlag(isCustom = false)
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun updateWorkout(workout: Workout) {
        workoutDao.updateWorkout(workout)
    }
}
