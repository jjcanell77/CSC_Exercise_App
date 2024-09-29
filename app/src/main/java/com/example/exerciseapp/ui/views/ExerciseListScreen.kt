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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.Exercise
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.Workout
import com.example.exerciseapp.ui.theme.ExerciseAppTheme

@Composable
fun ExerciseListScreen (
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
                title = workout.name,
                canNavigateBack = true,
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

@Composable
fun ListBody(
    exerciseList: List<Exercise>,
    modifier: Modifier = Modifier,
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
                            .clickable { /* TODO */ }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row (
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ){
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
fun ListScreenPreview () {
    ExerciseAppTheme {
        ExerciseListScreen()
    }
}