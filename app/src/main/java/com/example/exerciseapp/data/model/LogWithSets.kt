package com.example.exerciseapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class LogWithSets(
    @Embedded val log: Log,
    @Relation(
        parentColumn = "id",
        entityColumn = "logId"
    )
    val sets: List<ExerciseSet>
)