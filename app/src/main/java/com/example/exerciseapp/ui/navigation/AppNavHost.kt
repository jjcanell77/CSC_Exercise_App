
package com.example.exerciseapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.exerciseapp.ui.views.HomeDestination
import com.example.exerciseapp.ui.views.ExerciseListDestination
import com.example.exerciseapp.ui.views.ExerciseListScreen
import com.example.exerciseapp.ui.views.ExerciseScreen
import com.example.exerciseapp.ui.views.ProgramDestination
import com.example.exerciseapp.ui.views.LogEntryDestination
import com.example.exerciseapp.ui.views.LogEntryScreen
import com.example.exerciseapp.ui.views.ProgramScreen
import com.example.exerciseapp.ui.views.WorkoutDestination
import com.example.exerciseapp.ui.views.WorkoutScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            ExerciseScreen(
                navigateToProgram = { navController.navigate(ProgramDestination.route) },
                navigateToWorkout = { navController.navigate(WorkoutDestination.route) },
                navigateToExerciseList = { navController.navigate("${ExerciseListDestination.route}/${it}") }
            )
        }
        composable(route = ProgramDestination.route) {
            ProgramScreen(
                navigateToWorkout = { navController.navigate(WorkoutDestination.route) },
                navigateToExercise = { navController.navigate(HomeDestination.route) },
                navigateToExerciseList = { navController.navigate("${ExerciseListDestination.route}/${it}") }
            )
        }
        composable(route = WorkoutDestination.route) {
            WorkoutScreen(
                navigateToProgram = { navController.navigate(ProgramDestination.route) },
                navigateToExercise = { navController.navigate(HomeDestination.route) },
                navigateToExerciseList = { navController.navigate("${ExerciseListDestination.route}/${it}") }
            )
        }
        composable(
            route = ExerciseListDestination.routeWithArgs,
            arguments = listOf(navArgument(ExerciseListDestination.workoutIddArg) {
                type = NavType.IntType
            })
        ) {
            ExerciseListScreen(
                navigateBack = { navController.popBackStack() },
                navigateToLogEntry = { navController.navigate("${LogEntryDestination.route}/${it}") }
            )
        }
        composable(
            route = LogEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(LogEntryDestination.exerciseIdArg) {
                type = NavType.IntType
            })
        ) {
            LogEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}