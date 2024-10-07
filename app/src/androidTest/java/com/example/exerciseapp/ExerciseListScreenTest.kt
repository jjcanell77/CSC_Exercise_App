package com.example.exerciseapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.data.repository.IWorkoutRepository
import com.example.exerciseapp.testdata.FakeExerciseRepository
import com.example.exerciseapp.testdata.FakeWorkoutRepository
import com.example.exerciseapp.ui.viewmodels.ExerciseListViewModel
import com.example.exerciseapp.ui.views.ExerciseListScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var exerciseListViewModel: ExerciseListViewModel
    private lateinit var fakeExerciseRepository: IExerciseRepository
    private lateinit var fakeWorkoutRepository: IWorkoutRepository

    @Before
    fun setUp() {
        fakeExerciseRepository = FakeExerciseRepository()
        fakeWorkoutRepository = FakeWorkoutRepository(fakeExerciseRepository = fakeExerciseRepository as FakeExerciseRepository)

        exerciseListViewModel = ExerciseListViewModel(
            workoutRepository = fakeWorkoutRepository,
            exerciseRepository = fakeExerciseRepository,
            savedStateHandle = SavedStateHandle(mapOf("workoutId" to 1))
        )
    }

    @Test
    fun exerciseListScreen_displaysExercises() {
        composeTestRule.setContent {
            ExerciseListScreen(
                navController = rememberNavController(),
                navigateToLogEntry = {},
                onNavigateUp = {},
                exerciseListViewModel = exerciseListViewModel
            )
        }
        composeTestRule.onNodeWithText("Bench Press").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lateral Raises").assertIsDisplayed()
    }
}