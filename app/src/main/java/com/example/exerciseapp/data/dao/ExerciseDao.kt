package com.example.exerciseapp.data.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises WHERE typeId = :typeId")
    suspend fun getExercisesByMuscleGroup(typeId: Int): List<Exercise>

    @Transaction
    @Query("""
        SELECT e.* FROM exercises e
        INNER JOIN workout_exercise we ON e.id = we.exerciseId
        WHERE we.workoutId = :workoutId
    """)
    suspend fun getExercisesByWorkout(workoutId: Int): List<Exercise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExercises(exercises: List<Exercise>)

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: Int): Exercise

    @Query("SELECT * FROM muscle_group WHERE id = :id")
    suspend fun getMuscleGroupById(id: Int): MuscleGroup

    @Query("SELECT * FROM exercises WHERE name LIKE :query")
    suspend fun searchExercises(query: String): List<Exercise>

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)
}