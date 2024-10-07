package com.example.exerciseapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.testdata.FakeExerciseRepository
import com.example.exerciseapp.ui.viewmodels.MuscleGroupViewModel
import com.example.exerciseapp.ui.views.MuscleGroupScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MuscleGroupScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeExerciseRepository: IExerciseRepository
    private lateinit var muscleGroupViewModel: MuscleGroupViewModel

    @Before
    fun setUp() {
        fakeExerciseRepository = FakeExerciseRepository()
        val muscleGroupId = 1
        muscleGroupViewModel = MuscleGroupViewModel(
            exerciseRepository = fakeExerciseRepository,
            savedStateHandle = SavedStateHandle(mapOf("muscleGroupId" to muscleGroupId))
        )
    }

    @Test
    fun muscleGroupScreen_displaysExercises() {
        composeTestRule.setContent {
            MuscleGroupScreen(
                navController = rememberNavController(),
                onNavigateUp = {},
                navigateToLogEntry = {},
                muscleGroupViewModel = muscleGroupViewModel
            )
        }

        composeTestRule.onNodeWithText("Bench Press").assertIsDisplayed()
        composeTestRule.onNodeWithText("Push Up").assertIsDisplayed()
    }

    @Test
    fun muscleGroupScreen_addExercise() {
        composeTestRule.setContent {
            MuscleGroupScreen(
                navController = rememberNavController(),
                onNavigateUp = {},
                navigateToLogEntry = {},
                muscleGroupViewModel = muscleGroupViewModel
            )
        }

        composeTestRule.onNodeWithText("Add Exercise").performClick()

        composeTestRule.onNodeWithText("New Workout Name").performTextInput("Incline Bench Press")

        composeTestRule.onNodeWithText("OK").performClick()

        composeTestRule.onNodeWithText("Incline Bench Press").assertIsDisplayed()
    }
}
