
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.theme.ExerciseAppTheme
import com.example.exerciseapp.ui.viewmodels.ProgramViewModel

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
    programViewModel: ProgramViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val programList by programViewModel.programList.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(R.string.program_screen_title),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigation(
                navigateToWorkout = navigateToWorkout,
                navigateToExercise = navigateToExercise
            )
        }
    ){ innerPadding ->
        ProgramBody(
            workoutList = programList,
            contentPadding = innerPadding,
            onSelected = navigateToExerciseList
        )
    }
}

@Composable
fun ProgramBody(
    modifier: Modifier = Modifier,
    workoutList: List<Workout>,
    onSelected: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(contentPadding)
    ) {
        if(workoutList.isEmpty()){
            EmptyList()
        }
        else{
            LazyColumn{
                items(workoutList) { workout ->
                    ProgramCard(
                        workout = workout,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_xsmall))
                            .clickable{ onSelected(workout.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProgramCard(
    workout: Workout,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
    ) {
        Row (
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = workout.name,
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

@Preview(showBackground = true)
@Composable
fun ProgramBodyPreview () {
    val programList = listOf(
        Workout(
            id = 1,
            name = "Push"
        ),
        Workout(
            id = 2,
            name = "Pull"
        )
        ,
        Workout(
            id = 3,
            name = "Legs"
        )
    )

    ExerciseAppTheme {
        ProgramBody(workoutList = programList)
    }
}