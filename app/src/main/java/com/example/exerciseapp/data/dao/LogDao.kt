package com.example.exerciseapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets

@Dao
interface LogDao {

    @Insert
    suspend fun insertLog(log: Log): Long

    @Insert
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long

    @Insert
    suspend fun insertExerciseSets(sets: List<ExerciseSet>)

    @Transaction
    @Query("SELECT * FROM logs WHERE exerciseId = :exerciseId ORDER BY id DESC LIMIT 1")
    suspend fun getLatestLogWithSets(exerciseId: Int): LogWithSets?
}
