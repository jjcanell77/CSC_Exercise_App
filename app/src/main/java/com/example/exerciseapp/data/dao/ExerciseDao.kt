package com.example.exerciseapp.data.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.exerciseapp.data.model.Exercise

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE typeId = :typeId")
    suspend fun getExercisesByType(typeId: Int): List<Exercise>
}
