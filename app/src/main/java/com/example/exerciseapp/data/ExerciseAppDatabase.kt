package com.example.exerciseapp.data

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.exerciseapp.data.dao.ExerciseDao
import com.example.exerciseapp.data.dao.LogDao
import com.example.exerciseapp.data.dao.WorkoutDao
import com.example.exerciseapp.data.dao.WorkoutExerciseDao
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise

@Database(
    entities = [
        MuscleGroup::class,
        Exercise::class,
        Workout::class,
        WorkoutExercise::class,
        Log::class,
        ExerciseSet::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class ExerciseAppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun logDao(): LogDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao

    companion object {
        @Volatile
        private var instance: ExerciseAppDatabase? = null

        fun getDatabase(context: Context): ExerciseAppDatabase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseAppDatabase::class.java,
                    "app_database"
                ).build()
                instance = db
                db
            }
        }
    }
}