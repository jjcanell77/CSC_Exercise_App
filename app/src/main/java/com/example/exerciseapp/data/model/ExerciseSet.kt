package com.example.exerciseapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_sets",
    foreignKeys = [
        ForeignKey(
            entity = Log::class,
            parentColumns = ["id"],
            childColumns = ["logId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["logId"])]
)
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val logId: Int,
    val weight: Double = 0.0,
    val reps: Int = 0
)
