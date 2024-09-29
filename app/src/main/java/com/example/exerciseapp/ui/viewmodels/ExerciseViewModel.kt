package com.example.exerciseapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.exerciseapp.Exercise
import com.example.exerciseapp.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExerciseViewModel(private val workoutList: List<Workout>) : ViewModel() {

    // State for search text
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    // State to track if search is active
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // State to track if search is active
    private val _showList = MutableStateFlow(false)
    val showList: StateFlow<Boolean> = _showList.asStateFlow()

    private val _selectedWorkoutId = MutableStateFlow<Int?>(null)
    val selectedWorkoutId: StateFlow<Int?> = _selectedWorkoutId.asStateFlow()

    // Original list of countries
    private val fullExerciseList = listOf(
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
        ),
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
        ),
        Exercise(
            id = 11,
            name = "Bulgarian Split Squats"
        ),
        Exercise(
            id = 12,
            name = "Dumbell Bench Press"
        ),
        Exercise(
            id = 13,
            name = "Deadlift"
        ),
        Exercise(
            id = 14,
            name = "Pull-up"
        ),
        Exercise(
            id = 15,
            name = "Dumbbell Lateral Raise"
        )
    )

    // State for filtered list based on search query
    private val _exercisesList = MutableStateFlow(fullExerciseList)
    val exercisesList: StateFlow<List<Exercise>> = _exercisesList.asStateFlow()

    // Function to update search text
    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
        filterExercises(newText)
    }

    // Function to toggle search active state
    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
    }

    fun onToggleList(id: Int) {
        _showList.value = true
        _selectedWorkoutId.value = id
        getExercisesForWorkout(id)
    }

    fun onBackPressed() {
        _showList.value = false
        _selectedWorkoutId.value = null
        _exercisesList.value = fullExerciseList
    }

    // Function to close and reset search bar
    fun onCloseSearch() {
        onToggleSearch();
        onSearchTextChange("")
    }

    // Filtering logic
    private fun filterExercises(query: String) {
        _exercisesList.value = if (query.isEmpty()) {
            fullExerciseList
        } else {
            fullExerciseList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }

    fun getExercisesForWorkout(workoutId: Int) {
        val workout = workoutList.find { it.id == workoutId }
        _exercisesList.value = workout?.exercises ?: emptyList()
    }
}