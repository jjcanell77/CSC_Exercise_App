package com.example.exerciseapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "workout_exercise",
    primaryKeys = ["workoutId", "exerciseId"],
    indices = [
        Index(value = ["workoutId"]),
        Index(value = ["exerciseId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutExercise(
    val workoutId: Int,
    val exerciseId: Int
)
