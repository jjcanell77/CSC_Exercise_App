
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
import com.example.exerciseapp.ui.views.MuscleGroupSceen
import com.example.exerciseapp.ui.views.MuscleGroupSceenDestination
import com.example.exerciseapp.ui.views.ProgramScreen
import com.example.exerciseapp.ui.views.WorkoutDestination
import com.example.exerciseapp.ui.views.WorkoutEntryScreen
import com.example.exerciseapp.ui.views.WorkoutEntryScreenDestination
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
                navController = navController,
                navigateToExerciseList = { navController.navigate("${ExerciseListDestination.route}/${it}") }
            )
        }
        composable(route = ProgramDestination.route) {
            ProgramScreen(
                navController = navController,
                navigateToExerciseList = { navController.navigate("${ExerciseListDestination.route}/${it}") }
            )
        }
        composable(route = WorkoutDestination.route) {
            WorkoutScreen(
                navController = navController,
                navigateToWorkoutEntry = { navController.navigate(WorkoutEntryScreenDestination.route) },
                navigateToExerciseList = { navController.navigate("${ExerciseListDestination.route}/${it}") }
            )
        }
        composable(
            route = WorkoutEntryScreenDestination.route
        ) {
            WorkoutEntryScreen(
                navigateToWorkout = { navController.navigate(WorkoutDestination.route) },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = MuscleGroupSceenDestination.routeWithArgs,
            arguments = listOf(navArgument(MuscleGroupSceenDestination.muscleGroupIdArg) {
                type = NavType.IntType
            })
        ) {
            MuscleGroupSceen(
                navController = navController,
                navigateToLogEntry = { navController.navigate("${LogEntryDestination.route}/${it}") },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ExerciseListDestination.routeWithArgs,
            arguments = listOf(navArgument(ExerciseListDestination.workoutIdArg) {
                type = NavType.IntType
            })
        ) {
            ExerciseListScreen(
                navController = navController,
                navigateToLogEntry = { navController.navigate("${LogEntryDestination.route}/${it}") },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = LogEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(LogEntryDestination.exerciseIdArg) {
                type = NavType.IntType
            })
        ) {
            LogEntryScreen(
                navController = navController,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}