package com.example.exerciseapp.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.TopIcon
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.viewmodels.ExerciseListViewModel
import com.example.exerciseapp.ui.viewmodels.MuscleGroupViewModel

object MuscleGroupScreenDestination : NavigationDestination {
    override val route = "muscle_group_screen"
    override val titleRes = R.string.muscle_group_screen
    const val muscleGroupIdArg = "muscleGroupId"
    val routeWithArgs = "$route/{$muscleGroupIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateUp: () -> Unit,
    navigateToLogEntry: (Int) -> Unit,
    muscleGroupViewModel: MuscleGroupViewModel = viewModel(factory = AppViewModelProvider.Factory,),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val exerciseList by muscleGroupViewModel.exerciseList.collectAsState()
    val title by muscleGroupViewModel.title.collectAsState()
    var isEdit by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = title,
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
        MuscleGroupBody(
            exerciseList = exerciseList,
            contentPadding = innerPadding,
            closeEditMode = { isEdit = false },
            onSelected = navigateToLogEntry
        )
    }
}

@Composable
fun MuscleGroupBody(
    modifier: Modifier = Modifier,
    exerciseList: List<Exercise>,
    isEdit: Boolean = false,
    closeEditMode: () -> Unit = {},
    onSelected: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(contentPadding)
    ) {
        if(exerciseList.isEmpty()){
            EmptyList(text = stringResource(R.string.no_exercises_card))
        }
        else{
            LazyColumn{
                items(exerciseList) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        isEdit = isEdit,
                        closeEditMode = closeEditMode,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_xsmall))
                            .clickable { onSelected(exercise.id) }
                    )
                }
            }
        }
    }
}