package com.example.exerciseapp.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WorkoutWithExercises(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = WorkoutExercise::class,
            parentColumn = "workoutId",
            entityColumn = "exerciseId"
        )
    )
    val exercises: List<Exercise>
)