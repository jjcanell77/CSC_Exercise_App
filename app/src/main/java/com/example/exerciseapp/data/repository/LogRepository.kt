package com.example.exerciseapp.data.repository

import com.example.exerciseapp.data.dao.LogDao
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets

class LogRepository(private val logDao: LogDao) : ILogRepository {

    override suspend fun insertLog(log: Log): Long {
        return logDao.insertLog(log)
    }

    override suspend fun insertExerciseSets(sets: List<ExerciseSet>) {
        sets.forEach { set ->
            logDao.insertExerciseSet(set)
        }
    }

    override suspend fun getLatestLogForExercise(exerciseId: Int): LogWithSets? {
        return logDao.getLatestLogWithSets(exerciseId)
    }
}