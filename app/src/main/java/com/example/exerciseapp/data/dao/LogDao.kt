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

    @Transaction
    @Query("SELECT * FROM logs WHERE id = :logId")
    suspend fun getLogWithSets(logId: Int): LogWithSets

    @Query("SELECT * FROM logs WHERE exerciseId = :exerciseId")
    suspend fun getLogsForExercise(exerciseId: Int): List<Log>
}