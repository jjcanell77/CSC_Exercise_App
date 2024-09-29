package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.exerciseapp.R
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutViewModel : ViewModel() {

    // State for search text
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    // State to track if search is active
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Original list of countries
    private val fullWorkoutList = listOf(
        Workout(
            id = 1,
            name = "Abs",
            image = R.drawable.abs,
            exercises = listOf(
                Exercise(
                    id = 1,
                    name = "Ab Crunch Machine"
                ),
                Exercise(
                    id = 2,
                    name = "Abdominal Twist"
                ),
                Exercise(
                    id = 3,
                    name = "Barberll Rollout"
                ),
                Exercise(
                    id = 4,
                    name = "Bicycle Lick"
                ),
                Exercise(
                    id = 5,
                    name = "Leg Raises"
                )
            )
        ),
        Workout(
            id = 2,
            name = "Arms",
            image = R.drawable.arms,
            exercises = listOf(
                Exercise(
                    id = 6,
                    name = "Arnold Press"
                ),
                Exercise(
                    id = 7,
                    name = "Dips"
                ),
                Exercise(
                    id = 8,
                    name = "Cable Tricep Pulldowns"
                ),
                Exercise(
                    id = 9,
                    name = "Dumbbell Curls"
                ),
                Exercise(
                    id = 10,
                    name = "French Press"
                )
            )
        ),
        Workout(
            id = 3,
            name = "Legs",
            image = R.drawable.legs,
            exercises = listOf(
                Exercise(
                    id = 11,
                    name = "Bulgarian Split Squats"
                )
            )
        ),
        Workout(
            id = 4,
            name = "Back",
            image = R.drawable.back,
            exercises = listOf(
                Exercise(
                    id = 13,
                    name = "Deadlift"
                ), Exercise(
                    id = 14,
                    name = "Pull-up"
                )
            )
        ),
        Workout(
            id = 5,
            name = "Shoulders",
            image = R.drawable.shoulders,
            exercises = listOf(
                Exercise(
                    id = 6,
                    name = "Arnold Press"
                ),
                Exercise(
                    id = 10,
                    name = "French Press"
                ),
                Exercise(
                    id = 15,
                    name = "Dumbbell Lateral Raise"
                )
            )
        ),
        Workout(
            id = 6,
            name = "Chest",
            image = R.drawable.chest,
            exercises = listOf(
                Exercise(
                    id = 12,
                    name = "Dumbell Bench Press"
                )
            )
        ),
    )

    // State for filtered list based on search query
    private val _workoutsList = MutableStateFlow(fullWorkoutList)
    val workoutsList: StateFlow<List<Workout>> = _workoutsList.asStateFlow()

    // Function to update search text
    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
        filterExercises(newText)
    }

    // Function to toggle search active state
    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
    }

    // Function to close and reset search bar
    fun onCloseSearch() {
        onToggleSearch();
        onSearchTextChange("")
    }

    // Filtering logic
    private fun filterExercises(query: String) {
        _workoutsList.value = if (query.isEmpty()) {
            fullWorkoutList
        } else {
            fullWorkoutList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun getWorkout(id:Int){
        fullWorkoutList.filter { it.id == id }
    }
}