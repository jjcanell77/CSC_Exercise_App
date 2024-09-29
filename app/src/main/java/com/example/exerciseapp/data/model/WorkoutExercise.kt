package com.example.exerciseapp.data.model

import androidx.room.Entity

@Entity(
    tableName = "workout_exercise",
    primaryKeys = ["workoutId", "exerciseId"]
)
data class WorkoutExercise(
    val workoutId: Int,
    val exerciseId: Int
)