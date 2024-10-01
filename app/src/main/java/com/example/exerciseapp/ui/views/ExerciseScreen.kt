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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.R
import com.example.exerciseapp.TopSearchBar
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.theme.ExerciseAppTheme
import com.example.exerciseapp.ui.viewmodels.ExerciseViewModel

object HomeDestination : NavigationDestination {
    override val route = "exercise_screen"
    override val titleRes = R.string.exercise_screen_title
}

@Composable
fun ExerciseScreen (
    modifier: Modifier = Modifier,
    navigateToWorkout: () -> Unit,
    navigateToProgram: () -> Unit,
    navigateToExerciseList: (Int) -> Unit,
    exerciseViewModel: ExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val searchText by exerciseViewModel.searchText.collectAsState()
    val isSearching by exerciseViewModel.isSearching.collectAsState()
    val exerciseList by exerciseViewModel.exerciseList.collectAsState()
    val showList by exerciseViewModel.showList.collectAsState()
    val muscleGroupList by exerciseViewModel.muscleGroupList.collectAsState()

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
            BottomNavigation(
                navigateToWorkout = navigateToWorkout,
                navigateToProgram = navigateToProgram
            )
        }
    ){ innerPadding ->
        if(showList){
            ListBody(
                exerciseList = exerciseList,
                contentPadding = innerPadding,
                onSelected = navigateToExerciseList
            )
        } else{
            ExerciseBody(
                muscleGroupList = muscleGroupList,
                modifier = modifier.fillMaxSize(),
                contentPadding = innerPadding,
                onSelected = navigateToExerciseList,
            )
        }
    }
}

@Composable
private fun ExerciseBody(
    muscleGroupList: List<MuscleGroup>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onSelected: (Int) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding
        ) {
            items(muscleGroupList) { muscleGroup ->
                MuscleGroupCard(
                    muscleGroup = muscleGroup,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_xsmall))
                        .clickable { onSelected(muscleGroup.id) }
                )
            }
        }
    }
}

@Composable
private fun MuscleGroupCard(
    muscleGroup: MuscleGroup,
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
                painter = painterResource(id = muscleGroup.image),
                contentDescription = muscleGroup.name
            )

            Text(
                text = muscleGroup.name,
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
fun MuscleGroupCardPreview() {
    val muscleGroup = MuscleGroup(
                id = 1,
                name = "Abs",
                image = R.drawable.abs,
            )
    ExerciseAppTheme {
        MuscleGroupCard(muscleGroup)
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseScreenPreview () {
    val muscleGroupList = listOf(
        MuscleGroup(
            id = 1,
            name = "Abs",
            image = R.drawable.abs,
        ),MuscleGroup(
            id = 2,
            name = "Chest",
            image = R.drawable.chest,
        ),MuscleGroup(
            id = 3,
            name = "Legs",
            image = R.drawable.legs,
        )
    )
    ExerciseAppTheme {
        ExerciseBody(muscleGroupList)
    }
}