package com.example.exerciseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.model.WorkoutWithExercises

@Dao
interface WorkoutExerciseDao {
    @Insert
    suspend fun insertWorkoutExercise(crossRef: WorkoutExercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWorkoutExercises(workoutExercises: List<WorkoutExercise>): List<Long>

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises
}