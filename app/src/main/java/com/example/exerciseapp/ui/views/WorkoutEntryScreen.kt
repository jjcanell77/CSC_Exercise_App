package com.example.exerciseapp.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.TopIcon
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.viewmodels.ExerciseViewModel
import com.example.exerciseapp.ui.viewmodels.WorkoutViewModel

object WorkoutEntryScreenDestination : NavigationDestination {
    override val route = "workout_entry_screen"
    override val titleRes = R.string.workout_entry_screen
//    const val workoutIdArg = "workoutId"
//    val routeWithArgs = "$route/{$workoutIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutEntryScreen(
    modifier: Modifier = Modifier,
    navigateToWorkout: () -> Unit,
    onNavigateUp: () -> Unit,
    workoutViewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory),
    exerciseViewModel: ExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val newWorkoutName = workoutViewModel.newWorkoutName ?: ""
    var selectedExercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    val allExercises by exerciseViewModel.exerciseList.collectAsState()

    LaunchedEffect(Unit) {
        exerciseViewModel.getExercises()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = newWorkoutName,
                leftIcon = { TopIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", onClick = { onNavigateUp() }) },
                rightIcon = { SaveButton(onClick = {
                    // Create the new workout and navigate to WorkoutScreen
                    workoutViewModel.addWorkout(newWorkoutName, selectedExercises)
                    navigateToWorkout()
                }) },
                scrollBehavior = scrollBehavior
            )
        }
    ){ innerPadding ->
        WorkoutEntryBody(
            allExercises = allExercises,
            selectedExercises = selectedExercises,
            onExerciseSelected = { exercise ->
                selectedExercises = selectedExercises + exercise
            },
            onExerciseDeselected = { exercise ->
                selectedExercises = selectedExercises - exercise
            },
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun WorkoutEntryBody(
    allExercises: List<Exercise>,
    selectedExercises: List<Exercise>,
    onExerciseSelected: (Exercise) -> Unit,
    onExerciseDeselected: (Exercise) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(allExercises) { exercise ->
            val isSelected = selectedExercises.contains(exercise)
            ExerciseSelectableRow(
                exercise = exercise,
                isSelected = isSelected,
                onClick = {
                    if (isSelected) {
                        onExerciseDeselected(exercise)
                    } else {
                        onExerciseSelected(exercise)
                    }
                }
            )
        }
    }
}

@Composable
fun ExerciseSelectableRow(
    exercise: Exercise,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = exercise.name, modifier = Modifier.weight(1f))
        androidx.compose.material3.Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() }
        )
    }
}

//@Composable
//fun WorkoutEntryCard(
//    workout: Workout,
//    modifier: Modifier = Modifier,
//    workoutViewModel: WorkoutViewModel,
//    isAdded: Boolean
//) {
//    Card(
//        modifier = modifier
//    ) {
//        Row (
//            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            Text(
//                text = workout.name,
//                modifier = Modifier.padding(8.dp)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            if (isAdded){
//                Icon(
//                    modifier = Modifier.clickable { workoutViewModel.addExercise(workout)},
//                    imageVector = Icons.Outlined.Clear,
//                    contentDescription = "Clear"
//                )
//            }
//            else {
//                Icon(
//                    modifier = Modifier.clickable { workoutViewModel.removeExercise(workout)},
//                    imageVector = Icons.Filled.MoreVert,
//                    contentDescription = "Options"
//                )
//            }
//        }
//    }
//}

@Composable
fun SaveButton (
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ){
        Text(text = "Save")
    }
}