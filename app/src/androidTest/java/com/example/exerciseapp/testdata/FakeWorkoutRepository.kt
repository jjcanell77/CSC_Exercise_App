package com.example.exerciseapp.testdata

import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.data.model.WorkoutExercise
import com.example.exerciseapp.data.model.WorkoutWithExercises
import com.example.exerciseapp.data.repository.IWorkoutRepository

class FakeWorkoutRepository(private val fakeExerciseRepository: FakeExerciseRepository) : IWorkoutRepository {
    private val workouts = mutableListOf<Workout>()
    private val workoutExercises = mutableListOf<WorkoutExercise>()

    init {
        workouts.addAll(
            listOf(
                Workout(id = 1, name = "Push", isCustom = false),
                Workout(id = 2, name = "Pull", isCustom = false),
                Workout(id = 3, name = "Legs", isCustom = false),
                Workout(id = 4, name = "Custom 1", isCustom = true),
                Workout(id = 4, name = "Custom 2", isCustom = true)
            )
        )
         workoutExercises.addAll(
             listOf(
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
                 WorkoutExercise(workoutId = 3, exerciseId = 26),  // Legs - Leg Raises

                 WorkoutExercise(workoutId = 4, exerciseId = 1),  // Chest day - Bench Press
                 WorkoutExercise(workoutId = 4, exerciseId = 4), // Chest day - Push up
                 WorkoutExercise(workoutId = 4, exerciseId = 15), // Chest day - Front Raises
                 WorkoutExercise(workoutId = 4, exerciseId = 5),  // Chest day - Cable Crossovers
                 WorkoutExercise(workoutId = 4, exerciseId = 28), // Chest day - Russian Twists
            )
         )
    }

    override suspend fun insertWorkout(workout: Workout): Long {
        val newId = (workouts.maxOfOrNull { it.id } ?: 0) + 1
        workouts.add(workout.copy(id = newId))
        return newId.toLong()
    }

    override suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExercise) {
        workoutExercises.add(crossRef)
    }

    override suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises {
        val workout = workouts.first { it.id == workoutId }
        val exercises = workoutExercises
            .filter { it.workoutId == workoutId }
            .map { crossRef ->
                fakeExerciseRepository.getExerciseById(crossRef.exerciseId)

            }
        return WorkoutWithExercises(workout = workout, exercises = exercises)
    }

    override suspend fun getNonCustomWorkouts(): List<Workout> {
        return workouts.filter { !it.isCustom }
    }

    override suspend fun getCustomWorkouts(): List<Workout> {
        return workouts.filter { it.isCustom }
    }

    override suspend fun deleteWorkout(workout: Workout) {
        workouts.removeIf { it.id == workout.id }
        workoutExercises.removeIf { it.workoutId == workout.id }
    }

    override suspend fun updateWorkout(workout: Workout) {
        val index = workouts.indexOfFirst { it.id == workout.id }
        if (index != -1) {
            workouts[index] = workout
        }
    }
}
