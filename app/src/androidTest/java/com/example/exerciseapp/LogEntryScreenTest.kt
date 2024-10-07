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
import com.example.exerciseapp.data.repository.ILogRepository
import com.example.exerciseapp.testdata.FakeExerciseRepository
import com.example.exerciseapp.testdata.FakeLogRepository
import com.example.exerciseapp.ui.viewmodels.LogViewModel
import com.example.exerciseapp.ui.views.LogEntryScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogEntryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeExerciseRepository: IExerciseRepository
    private lateinit var fakeLogRepository: ILogRepository
    private lateinit var logViewModel: LogViewModel

    @Before
    fun setUp() {
        fakeExerciseRepository = FakeExerciseRepository()
        fakeLogRepository = FakeLogRepository()
        val exerciseId = 1
        logViewModel = LogViewModel(
            logRepository = fakeLogRepository,
            exerciseRepository = fakeExerciseRepository,
            savedStateHandle = SavedStateHandle(mapOf("exerciseId" to exerciseId))
        )
    }

    @Test
    fun logEntryScreen_canAddSetAndSubmitLog() {
        composeTestRule.setContent {
            LogEntryScreen(
                navController = rememberNavController(),
                onNavigateUp = {},
                logViewModel = logViewModel
            )
        }

        composeTestRule.onNodeWithText("Weight").performTextInput("100")
        composeTestRule.onNodeWithText("Reps").performTextInput("10")

        composeTestRule.onNodeWithText("Add").performClick()

        composeTestRule.onNodeWithText("Set 1: 100.0 kg x 10 reps").assertIsDisplayed()

        composeTestRule.onNodeWithText("Submit").performClick()

        composeTestRule.onNodeWithText("Set 1: 100.0 kg x 10 reps").assertDoesNotExist()
    }
}