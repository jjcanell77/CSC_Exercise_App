package com.example.exerciseapp.testdata

import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.repository.IExerciseRepository

class FakeExerciseRepository : IExerciseRepository {
    private val muscleGroups = mutableListOf<MuscleGroup>()
    private val exercises = mutableListOf<Exercise>()

    init {
        // Initialize with sample data
        muscleGroups.addAll(
            listOf(
                MuscleGroup(id = 1, name = "Chest", imageName = "chest"),
                MuscleGroup(id = 2, name = "Back", imageName = "back"),
                MuscleGroup(id = 3, name = "Shoulders", imageName = "shoulders"),
                MuscleGroup(id = 4, name = "Arms", imageName = "arms"),
                MuscleGroup(id = 5, name = "Legs", imageName = "legs"),
                MuscleGroup(id = 6, name = "Abs", imageName = "abs")
            )
        )

        exercises.addAll(
            listOf(
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
        )
    }

    override suspend fun getAllExercises(): List<Exercise> = exercises

    override suspend fun getAllMuscleGroups(): List<MuscleGroup> = muscleGroups

    override suspend fun getExercisesByMuscleGroup(typeId: Int): List<Exercise> {
        return exercises.filter { it.typeId == typeId }
    }

    override suspend fun getExerciseById(id: Int): Exercise {
        return exercises.first { it.id == id }
    }

    override suspend fun getMuscleGroupById(id: Int): MuscleGroup {
        return muscleGroups.first { it.id == id }
    }

    override suspend fun searchExercises(query: String): List<Exercise> {
        return exercises.filter { it.name.contains(query, ignoreCase = true) }
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        exercises.removeIf { it.id == exercise.id }
    }

    override suspend fun updateExercise(exercise: Exercise) {
        val index = exercises.indexOfFirst { it.id == exercise.id }
        if (index != -1) {
            exercises[index] = exercise
        }
    }

    override suspend fun addExercise(exercise: Exercise): Long {
        val newId = (exercises.maxOfOrNull { it.id } ?: 0) + 1
        exercises.add(exercise.copy(id = newId))
        return newId.toLong()
    }
}
