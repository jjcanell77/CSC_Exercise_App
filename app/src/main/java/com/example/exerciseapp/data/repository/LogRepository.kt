package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.LogDao
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets

class LogRepository(private val logDao: LogDao) {

    suspend fun getLatestLogForExercise(exerciseId: Int): LogWithSets? {
        return logDao.getLatestLogWithSets(exerciseId)
    }

    suspend fun insertLogWithSets(log: Log, sets: List<ExerciseSet>) {
        val logId = logDao.insertLog(log).toInt()
        sets.forEach { set ->
            logDao.insertExerciseSet(set.copy(logId = logId))
        }
    }
}