package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.LogDao
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets

class LogRepository(private val logDao: LogDao) {

    suspend fun insertLog(log: Log): Long {
        return logDao.insertLog(log)
    }

    suspend fun insertExerciseSets(sets: List<ExerciseSet>) {
        sets.forEach { set ->
            logDao.insertExerciseSet(set)
        }
    }

    suspend fun getLatestLogForExercise(exerciseId: Int): LogWithSets? {
        return logDao.getLatestLogWithSets(exerciseId)
    }
}