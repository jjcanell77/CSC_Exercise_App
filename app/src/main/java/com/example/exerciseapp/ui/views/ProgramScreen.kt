
@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.exerciseapp.ui.views

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.theme.ExerciseAppTheme

object ProgramDestination : NavigationDestination {
    override val route = "program_screen"
    override val titleRes = R.string.program_screen_title
}

@Composable
fun ProgramScreen (
    navigateToWorkout: () -> Unit,
    navigateToExercise: () -> Unit,
    navigateToExerciseList: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val workout = Workout(
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
    )
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.program_screen_title),
                canEdit = false,
                canAdd = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigation()
        }
    ){ innerPadding ->
        ListBody(
            exerciseList = workout.exercises,
            contentPadding = innerPadding,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramScreennPreview () {
    ExerciseAppTheme {
        ProgramScreen()
    }
}