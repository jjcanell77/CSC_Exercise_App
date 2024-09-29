package com.example.exerciseapp.data.model

import kotlinx.datetime.LocalDateTime
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "logs",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["exerciseId"])]
)
data class Log(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val sets: List<ExerciseSet>, // Renamed from 'set' to 'sets' for clarity
    val notes: String,
    val date: LocalDateTime
)