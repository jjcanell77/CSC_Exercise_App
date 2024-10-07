package com.example.exerciseapp.testdata

import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.LogWithSets
import com.example.exerciseapp.data.repository.ILogRepository

class FakeLogRepository : ILogRepository {
    private val logs = mutableListOf<Log>()
    private val exerciseSets = mutableListOf<ExerciseSet>()

    override suspend fun insertLog(log: Log): Long {
        val newId = (logs.maxOfOrNull { it.id } ?: 0) + 1
        logs.add(log.copy(id = newId))
        return newId.toLong()
    }

    override suspend fun insertExerciseSets(sets: List<ExerciseSet>) {
        exerciseSets.addAll(sets)
    }

    override suspend fun getLatestLogForExercise(exerciseId: Int): LogWithSets? {
        val log = logs.filter { it.exerciseId == exerciseId }.maxByOrNull { it.id } ?: return null
        val sets = exerciseSets.filter { it.logId == log.id }
        return LogWithSets(log = log, sets = sets)
    }
}
