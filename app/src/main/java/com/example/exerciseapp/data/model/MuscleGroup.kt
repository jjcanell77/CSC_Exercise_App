package com.example.exerciseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "muscle_group")
data class MuscleGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageName: String
)