package com.example.exerciseapp.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.exerciseapp.data.model.MuscleGroup
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
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
    val muscleGroupList by muscleGroupViewModel.muscleGroupList.collectAsState()
    var showEntryModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = title,
                rightIcon ={
                    if(exerciseList.isNotEmpty() && exerciseList.any{ exercise -> exercise.isCustom}){
                        TopIcon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            onClick = {isEdit = !isEdit}
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                leftIcon ={
                    TopIcon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back", onClick = {onNavigateUp()}
                    ) },
            )
        },
        bottomBar = {
            Column {
                Button(
                    onClick = { showEntryModal = true  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_small))
                ) {
                    Text(text = "Add Exercise")
                }
            BottomNavigation(
                navController = navController,
            )
            }
        }
    ){ innerPadding ->
        MuscleGroupBody(
            exerciseList = exerciseList,
            muscleGroupList = muscleGroupList,
            contentPadding = innerPadding,
            closeEditMode = { isEdit = false },
            isEdit = isEdit,
            onUpdate = {  exercise ->
                muscleGroupViewModel.updateExercise(exercise)},
            onDelete = { exercise ->
                muscleGroupViewModel.deleteExercise(exercise) },
            onSelected = navigateToLogEntry
        )
        if (showEntryModal) {
            WorkoutModal(
                title= "Add Exercise",
                currentName = "",
                onConfirm = {  newName ->
                    showEntryModal = false
                    muscleGroupViewModel.addExercise(newName)
                },
                onDismiss = {
                    showEntryModal = false
                }
            )
        }
    }
}

@Composable
fun MuscleGroupBody(
    modifier: Modifier = Modifier,
    muscleGroupList: List<MuscleGroup>,
    exerciseList: List<Exercise>,
    isEdit: Boolean = false,
    onUpdate: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit = {},
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
                        muscleGroupList = muscleGroupList,
                        isEdit = isEdit,
                        onUpdate = { exercise -> onUpdate(exercise)},
                        onDelete = { exerciseD -> onDelete(exerciseD) },
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