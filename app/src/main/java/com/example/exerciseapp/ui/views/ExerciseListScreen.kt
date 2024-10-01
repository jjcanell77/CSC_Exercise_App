@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.exerciseapp.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.theme.ExerciseAppTheme
import com.example.exerciseapp.ui.viewmodels.ExerciseListViewModel

object ExerciseListDestination : NavigationDestination {
    override val route = "exercise_list_screen"
    override val titleRes = R.string.exercise_list_screen
    const val workoutIddArg = "workoutId"
    val routeWithArgs = "$route/{$workoutIddArg}"
}

@Composable
fun ExerciseListScreen(
    modifier: Modifier = Modifier,
    workoutId: Int = 0,
    navigateToExercise: () -> Unit,
    navigateToWorkout: () -> Unit,
    navigateToProgram: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToLogEntry: (Int) -> Unit,
    exerciseListViewModel: ExerciseListViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        key = "ExerciseListViewModel-$workoutId"
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
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateUp
            )
        },
        bottomBar = {
            BottomNavigation(
                navigateToExercise = navigateToExercise,
                navigateToWorkout = navigateToWorkout,
                navigateToProgram = navigateToProgram,
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

@Composable
fun ListBody(
    modifier: Modifier = Modifier,
    exerciseList: List<Exercise>,
    onSelected: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(contentPadding)
    ) {
        if(exerciseList.isEmpty()){
            EmptyList()
        }
        else{
            LazyColumn{
                items(exerciseList) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_xsmall))
                            .clickable { onSelected(exercise.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    modifier: Modifier = Modifier,
    isEdit: Boolean = false
) {
    Card(
        modifier = modifier
    ) {
        Row (
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (isEdit){
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Clear"
                )
                Text(
                    text = exercise.name,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "rename workout")
                }
            }
            else {
                Text(
                    text = exercise.name,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Options"
                )
            }
        }
    }
}

@Composable
fun EmptyList(
    modifier: Modifier = Modifier
){
    Card(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
    ) {
        Text(
            text = stringResource(R.string.no_excercises_card),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseCardPreview() {
    val exercise =
        Exercise(
            id = 1,
            name = "Bench Press",
            typeId = 1
        )
    ExerciseAppTheme {
        ExerciseCard(exercise)
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseCardEditPreview() {
    val exercise =
        Exercise(
            id = 1,
            name = "Bench Press",
            typeId = 1
        )
    ExerciseAppTheme {
        ExerciseCard(exercise, isEdit = true)
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyListPreview() {
    ExerciseAppTheme {
        EmptyList()
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview () {
    val exerciseList = listOf(
        Exercise(
            id = 1,
            name = "Bench Press",
            typeId = 1
        ),
        Exercise(
            id = 2,
            name = "Squat",
            typeId = 1
        )
        ,
        Exercise(
            id = 3,
            name = "Deadlift",
            typeId = 1
        )
    )
    ExerciseAppTheme {
        ListBody(exerciseList = exerciseList)
    }
}