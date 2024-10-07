package com.example.exerciseapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exerciseapp.data.repository.IExerciseRepository
import com.example.exerciseapp.data.repository.IWorkoutRepository
import com.example.exerciseapp.testdata.FakeExerciseRepository
import com.example.exerciseapp.testdata.FakeWorkoutRepository
import com.example.exerciseapp.ui.viewmodels.ProgramViewModel
import com.example.exerciseapp.ui.views.ProgramScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProgramScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeExerciseRepository: IExerciseRepository
    private lateinit var fakeWorkoutRepository: IWorkoutRepository
    private lateinit var programViewModel: ProgramViewModel

    @Before
    fun setUp() {
        fakeExerciseRepository = FakeExerciseRepository()
        fakeWorkoutRepository = FakeWorkoutRepository(fakeExerciseRepository = fakeExerciseRepository as FakeExerciseRepository)

        programViewModel = ProgramViewModel(
            workoutRepository = fakeWorkoutRepository
        )
    }

    @Test
    fun programScreen_displaysPrograms() {
        composeTestRule.setContent {
            ProgramScreen(
                navController = rememberNavController(),
                navigateToExerciseList = {},
                programViewModel = programViewModel
            )
        }

        composeTestRule.onNodeWithText("Push").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pull").assertIsDisplayed()
    }
}
