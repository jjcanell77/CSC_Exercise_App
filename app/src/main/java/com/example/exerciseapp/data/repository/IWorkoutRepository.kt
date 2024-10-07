package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.model.WorkoutWithExercises

interface IWorkoutRepository {
    suspend fun insertWorkout(workout: Workout): Long
    suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExercise)
    suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises
    suspend fun getNonCustomWorkouts(): List<Workout>
    suspend fun getCustomWorkouts(): List<Workout>
    suspend fun deleteWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
}