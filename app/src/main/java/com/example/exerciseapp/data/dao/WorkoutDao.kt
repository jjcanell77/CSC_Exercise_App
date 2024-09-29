package com.example.exerciseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.model.WorkoutWithExercises

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Insert
    suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExercise)

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises

    @Query("SELECT * FROM workouts")
    suspend fun getAllWorkouts(): List<Workout>
}