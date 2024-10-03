package com.example.exerciseapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.exerciseapp.data.dao.ExerciseDao
import com.example.exerciseapp.data.dao.LogDao
import com.example.exerciseapp.data.dao.MuscleGroupDao
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
    abstract fun muscleGroupDao(): MuscleGroupDao

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
            val muscleGroupList = listOf(
                MuscleGroup(id = 1, name = "Chest", imageName = "chest"),
                MuscleGroup(id = 2, name = "Back", imageName = "back"),
                MuscleGroup(id = 3, name = "Shoulders", imageName = "shoulders"),
                MuscleGroup(id = 4, name = "Arms", imageName = "arms"),
                MuscleGroup(id = 5, name = "Legs", imageName = "legs"),
                MuscleGroup(id = 6, name = "Abs", imageName = "abs")
            )
            // Insert muscle groups
            database.muscleGroupDao().insertAllMuscleGroups(muscleGroupList)

            val exercises = listOf(
                Exercise(id = 1, name = "Bench Press", typeId = 1),  // Chest
                Exercise(id = 2, name = "Dumbbell Incline Bench Press", typeId = 1),  // Chest
                Exercise(id = 3, name = "Machine Chest Fly", typeId = 1),  // Chest
                Exercise(id = 4, name = "Push Up", typeId = 1),  // Chest
                Exercise(id = 5, name = "Cable Crossovers", typeId = 1),  // Chest
                Exercise(id = 6, name = "Pull-Up", typeId = 2),  // Back
                Exercise(id = 7, name = "Dead-lift", typeId = 2),  // Back
                Exercise(id = 8, name = "Dumbbell Row", typeId = 2),  // Back
                Exercise(id = 9, name = "Lat Pull-down", typeId = 2),  // Back
                Exercise(id = 10, name = "Shrugs", typeId = 2),  // Back
                Exercise(id = 11, name = "Shoulder Press", typeId = 3),  // Shoulders
                Exercise(id = 12, name = "Lateral Raises", typeId = 3),  // Shoulders
                Exercise(id = 13, name = "Rear Raises", typeId = 3),  // Shoulders
                Exercise(id = 14, name = "Face-pulls", typeId = 3),  // Shoulders
                Exercise(id = 15, name = "Front Raises", typeId = 3),  // Shoulders
                Exercise(id = 16, name = "Barbell Curls", typeId = 4),  // Arms
                Exercise(id = 17, name = "Skull Crusher", typeId = 4),  // Arms
                Exercise(id = 18, name = "Incline Dumbbell Curl", typeId = 4),  // Arms
                Exercise(id = 19, name = "Push-Down", typeId = 4),  // Arms
                Exercise(id = 20, name = "Power Bombs", typeId = 4),  // Arms
                Exercise(id = 21, name = "Squat", typeId = 5),  // Legs
                Exercise(id = 22, name = "Bulgarian Split Squat", typeId = 5),  // Legs
                Exercise(id = 23, name = "Machine Leg Curl", typeId = 5),  // Legs
                Exercise(id = 24, name = "Machine Leg Extension", typeId = 5),  // Legs
                Exercise(id = 25, name = "Calf Raises", typeId = 5),  // Legs
                Exercise(id = 26, name = "Leg Raises", typeId = 6),  // Abs
                Exercise(id = 27, name = "Crunches", typeId = 6),  // Abs
                Exercise(id = 28, name = "Russian Twists", typeId = 6)  // Abs
            )
            // Insert exercises
            database.exerciseDao().insertAllExercises(exercises)

            // Prepopulate workouts with predefined IDs
            val workouts = listOf(
                Workout(id = 1, name = "Push", isCustom = false),
                Workout(id = 2, name = "Pull", isCustom = false),
                Workout(id = 3, name = "Legs", isCustom = false)
            )
            // Insert workouts
            database.workoutDao().insertAllWorkouts(workouts)

            // Associate exercises with workouts using predefined IDs
            val workoutExercises = listOf(
                WorkoutExercise(workoutId = 1, exerciseId = 1),  // Push - Bench Press
                WorkoutExercise(workoutId = 1, exerciseId = 12), // Push - Lateral Raises
                WorkoutExercise(workoutId = 1, exerciseId = 15), // Push - Front Raises
                WorkoutExercise(workoutId = 1, exerciseId = 5),  // Push - Cable Crossovers
                WorkoutExercise(workoutId = 1, exerciseId = 28), // Push - Russian Twists

                WorkoutExercise(workoutId = 2, exerciseId = 7),  // Pull - Dead-lift
                WorkoutExercise(workoutId = 2, exerciseId = 9),  // Pull - Lat Pull-down
                WorkoutExercise(workoutId = 2, exerciseId = 16), // Pull - Barbell Curls
                WorkoutExercise(workoutId = 2, exerciseId = 5),  // Pull - Cable Crossovers
                WorkoutExercise(workoutId = 2, exerciseId = 27), // Pull - Crunches

                WorkoutExercise(workoutId = 3, exerciseId = 21), // Legs - Squat
                WorkoutExercise(workoutId = 3, exerciseId = 22), // Legs - Bulgarian Split Squat
                WorkoutExercise(workoutId = 3, exerciseId = 24), // Legs - Machine Leg Extension
                WorkoutExercise(workoutId = 3, exerciseId = 25), // Legs - Calf Raises
                WorkoutExercise(workoutId = 3, exerciseId = 26)  // Legs - Leg Raises
            )
            // Insert workout exercises
            database.workoutExerciseDao().insertAllWorkoutExercises(workoutExercises)
        }
    }
}