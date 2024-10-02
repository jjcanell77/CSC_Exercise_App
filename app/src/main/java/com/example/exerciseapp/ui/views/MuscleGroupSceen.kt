package com.example.exerciseapp.ui.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.TopIcon
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.viewmodels.ExerciseListViewModel

object MuscleGroupSceenDestination : NavigationDestination {
    override val route = "exercise_list_screen"
    override val titleRes = R.string.muscle_group_screen
    const val muscleGroupIdArg = "muscleGroupId"
    val routeWithArgs = "$route/{$muscleGroupIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupSceen(
    modifier: Modifier = Modifier,
    muscleGroupId: Int = 0,
    navController: NavController,
    onNavigateUp: () -> Unit,
    navigateToLogEntry: (Int) -> Unit,
    exerciseListViewModel: ExerciseListViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        key = "ExerciseListViewModel-$muscleGroupId"
    )
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val exerciseList by exerciseListViewModel.exercises.collectAsState()
    val workout by exerciseListViewModel.workout.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = workout?.name ?: stringResource(R.string.exercise_list_screen),
                scrollBehavior = scrollBehavior,
                leftIcon ={ TopIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", onClick = {onNavigateUp()}) },
            )
        },
        bottomBar = {
            BottomNavigation(
                navController = navController
            )
        }
    ){ innerPadding ->
        ListBody(
            exerciseList = exerciseList,
            contentPadding = innerPadding,
            onSelected = navigateToLogEntry
        )
    }
}