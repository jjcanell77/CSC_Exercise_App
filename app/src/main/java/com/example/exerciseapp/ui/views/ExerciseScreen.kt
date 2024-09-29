package com.example.exerciseapp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.R
import com.example.exerciseapp.TopSearchBar
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.theme.ExerciseAppTheme
import com.example.exerciseapp.ui.viewmodels.ExerciseViewModel
import com.example.exerciseapp.ui.viewmodels.WorkoutViewModel

object HomeDestination : NavigationDestination {
    override val route = "exercise_screen"
    override val titleRes = R.string.exercise_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen (
    modifier: Modifier = Modifier,
    navigateToWorkout: () -> Unit,
    navigateToProgram: () -> Unit,
    navigateToExerciseList: (Int) -> Unit,
//    exerciseViewModel: ExerciseViewModel = ExerciseViewModel(),
    workoutViewModel: WorkoutViewModel = WorkoutViewModel()
) {
    // Collecting states from ViewModel
    val workoutList by workoutViewModel.workoutsList.collectAsState()
    val exerciseViewModel = remember { ExerciseViewModel(workoutList) }

    val searchText by exerciseViewModel.searchText.collectAsState()
    val isSearching by exerciseViewModel.isSearching.collectAsState()
    val exerciseList by exerciseViewModel.exercisesList.collectAsState()
    val showList by exerciseViewModel.showList.collectAsState()
    Scaffold(
        topBar = {
            TopSearchBar(
                searchText = searchText, // text showed on SearchBar
                onSearchTextChange = exerciseViewModel::onSearchTextChange, // update the value of searchText
                isSearching = isSearching, // whether the user is searching or not
                onToggleSearch = { exerciseViewModel.onToggleSearch() }, // toggles search state
                close = { exerciseViewModel.onCloseSearch() },
                exerciseList = exerciseList,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
        },
        bottomBar = {
            BottomNavigation()
        }
    ){ innerPadding ->
        ExerciseBody(
            workoutList = workoutList,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
            onSelected = exerciseViewModel::onToggleList,
            onBack = exerciseViewModel::onBackPressed,
            showList = showList
        )
    }
}

@Composable
private fun ExerciseBody(
    workoutList: List<Workout>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onSelected: (Int) -> Unit = {},
    onBack: () -> Unit = {},
    showList: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (showList) {
           // navigate to ListScreen
            // canAdd = false
        } else {
            // Display Workouts List
            LazyColumn(
                modifier = modifier,
                contentPadding = contentPadding
            ) {
                items(workoutList) { workout ->
                    WorkoutCard(
                        workout = workout,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_xsmall))
                            .clickable { onSelected(workout.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutCard(
    workout: Workout,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row ( modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen.padding_medium),
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = workout.image),
                contentDescription = workout.name
            )

            Text(
                text = workout.name,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Options"
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ExerciseListPreview() {
    val exercise =
        Exercise(
            id = 1,
            name = "Bench Press",
            muscleGroup = MuscleGroup(
                id = 1,
                name = "Abs",
                image = R.drawable.abs,
            )
        )

    ExerciseAppTheme {
        ExerciseCard(exercise)
    }
}
@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview () {
    ExerciseAppTheme {
        ExerciseScreen()
    }
}