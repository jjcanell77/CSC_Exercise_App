package com.example.exerciseapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.testdata.FakeExerciseRepository
import com.example.exerciseapp.ui.viewmodels.ExerciseViewModel
import com.example.exerciseapp.ui.views.ExerciseScreen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeExerciseRepository: IExerciseRepository
    private lateinit var exerciseViewModel: ExerciseViewModel

    @Before
    fun setUp() {
        fakeExerciseRepository = FakeExerciseRepository()
        exerciseViewModel = ExerciseViewModel(
            exerciseRepository = fakeExerciseRepository
        )
    }

    @Test
    fun exerciseScreen_displaysMuscleGroups() {
        composeTestRule.setContent {
            ExerciseScreen(
                navController = rememberNavController(),
                navigateToMuscleGroupScreen = {},
                navigateToLogEntry = {},
                exerciseViewModel = exerciseViewModel
            )
        }
        composeTestRule.onNodeWithText("Chest").assertIsDisplayed()
        composeTestRule.onNodeWithText("Back").assertIsDisplayed()
    }

    @Test
    fun exerciseScreen_navigateToMuscleGroupScreen() {
        var navigatedToMuscleGroupId: Int? = null

        composeTestRule.setContent {
            ExerciseScreen(
                navController = rememberNavController(),
                navigateToMuscleGroupScreen = { muscleGroupId ->
                    navigatedToMuscleGroupId = muscleGroupId
                },
                navigateToLogEntry = {},
                exerciseViewModel = exerciseViewModel
            )
        }

        composeTestRule.onNodeWithText("Chest").performClick()
        assertEquals(1, navigatedToMuscleGroupId)
    }
}