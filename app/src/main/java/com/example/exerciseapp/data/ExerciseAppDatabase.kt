package com.example.exerciseapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.exerciseapp.data.dao.ExerciseDao
import com.example.exerciseapp.data.dao.LogDao
import com.example.exerciseapp.data.dao.WorkoutDao
import com.example.exerciseapp.data.dao.WorkoutExerciseDao
import com.example.exerciseapp.data.database.MIGRATION_1_2
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.ExerciseSet
import com.example.exerciseapp.data.model.Log
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log as Log1

@Database(
    entities = [
        MuscleGroup::class,
        Exercise::class,
        Workout::class,
        WorkoutExercise::class,
        Log::class,
        ExerciseSet::class
    ],
    version = 2,
    exportSchema = false
)

abstract class ExerciseAppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun logDao(): LogDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao

    companion object {
        @Volatile
        private var Instance: ExerciseAppDatabase? = null

        fun getDatabase(context: Context): ExerciseAppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseAppDatabase::class.java,
                    "exercise_app_database"
                )
                    .addCallback(ExerciseAppDatabaseCallback())
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }
    }

    private class ExerciseAppDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log1.d("ExerciseAppDatabase", "onCreate called")

            // Insert data on a background thread
            CoroutineScope(Dispatchers.IO).launch {
                prepopulateDatabase(Instance!!)
            }
        }

        private suspend fun prepopulateDatabase(database: ExerciseAppDatabase) {
            Log1.d("ExerciseAppDatabase", "Prepopulating database")
            val muscleGroupDao = database.exerciseDao()
            val muscleGroupList = listOf(
                MuscleGroup(name = "Chest", imageName = "chest"),
                MuscleGroup(name = "Back", imageName = "back"),
                MuscleGroup(name = "Shoulders", imageName = "shoulders"),
                MuscleGroup(name = "Arms", imageName = "arms"),
                MuscleGroup(name = "Legs", imageName = "legs"),
                MuscleGroup(name = "Abs", imageName = "abs")
            )
            muscleGroupDao.insertAll(muscleGroupList)
        }

    }
}