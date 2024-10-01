package com.example.exerciseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.exerciseapp.data.model.WorkoutExercise

@Dao
interface WorkoutExerciseDao {
    @Insert
    suspend fun insertWorkoutExercise(crossRef: WorkoutExercise)
}