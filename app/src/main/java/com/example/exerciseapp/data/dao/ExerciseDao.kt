package com.example.exerciseapp.data.dao
import androidx.room.Dao
import androidx.room.Query
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM muscle_group")
    suspend fun getAllMuscleGroups(): List<MuscleGroup>

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE typeId = :typeId")
    suspend fun getExercisesByMuscleGroup(typeId: Int): List<Exercise>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: Int): Exercise

    @Query("SELECT * FROM exercises WHERE name LIKE :query")
    suspend fun searchExercises(query: String): List<Exercise>
}