package com.example.exerciseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exerciseapp.data.model.MuscleGroup

@Dao
interface MuscleGroupDao {

    @Query("SELECT * FROM muscle_group")
    suspend fun getAllMuscleGroups(): List<MuscleGroup>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMuscleGroups(muscleGroups: List<MuscleGroup>)
}