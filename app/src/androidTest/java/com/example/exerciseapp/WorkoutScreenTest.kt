package com.example.exerciseapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.data.repository.IWorkoutRepository
import com.example.exerciseapp.testdata.FakeExerciseRepository
import com.example.exerciseapp.testdata.FakeWorkoutRepository
import com.example.exerciseapp.ui.viewmodels.WorkoutViewModel
import com.example.exerciseapp.ui.views.WorkoutScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeExerciseRepository: IExerciseRepository
    private lateinit var fakeWorkoutRepository: IWorkoutRepository
    private lateinit var workoutViewModel: WorkoutViewModel

    @Before
     fun setUp() {
        fakeExerciseRepository = FakeExerciseRepository()
        fakeWorkoutRepository = FakeWorkoutRepository(fakeExerciseRepository = fakeExerciseRepository as FakeExerciseRepository)

        workoutViewModel = WorkoutViewModel(
            workoutRepository = fakeWorkoutRepository
        )
    }

    @Test
    fun workoutScreen_displaysWorkouts() {
        composeTestRule.setContent {
            WorkoutScreen(
                navController = rememberNavController(),
                navigateToWorkoutEntry = {},
                navigateToExerciseList = {},
                workoutViewModel = workoutViewModel
            )
        }

        composeTestRule.onNodeWithText("Custom 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Custom 2").assertIsDisplayed()
    }

    @Test
    fun workoutScreen_editModeAllowsDeletion() {
        composeTestRule.setContent {
            WorkoutScreen(
                navController = rememberNavController(),
                navigateToWorkoutEntry = {},
                navigateToExerciseList = {},
                workoutViewModel = workoutViewModel
            )
        }

        composeTestRule.onNodeWithContentDescription("Edit").performClick()

        composeTestRule.onAllNodesWithContentDescription("Clear")[0].performClick()

        composeTestRule.onNodeWithText("Custom Workout 1").assertDoesNotExist()
    }
}