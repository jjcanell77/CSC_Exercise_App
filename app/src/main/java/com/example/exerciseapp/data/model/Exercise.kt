package com.example.exerciseapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = MuscleGroup::class,
            parentColumns = ["id"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["typeId"])]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val typeId: Int,
    val isCustom: Boolean = false
)