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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.exerciseapp.ui.theme.ExerciseAppTheme
import com.example.exerciseapp.ui.viewmodels.ExerciseListViewModel

object ExerciseListDestination : NavigationDestination {
    override val route = "exercise_list_screen"
    override val titleRes = R.string.exercise_list_screen
    const val workoutIdArg = "workoutId"
    val routeWithWorkoutId = "$route/{$workoutIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateUp: () -> Unit,
    navigateToLogEntry: (Int) -> Unit,
    exerciseListViewModel: ExerciseListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val exercises by exerciseListViewModel.exerciseList.collectAsState()
    val title by exerciseListViewModel.title.collectAsState()
    var isEdit by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = title,
                scrollBehavior = scrollBehavior,
                leftIcon ={ TopIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", onClick = {onNavigateUp()}) },
                rightIcon = {
                    if(exercises.isNotEmpty()){
                        TopIcon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", onClick = {isEdit = !isEdit})
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                navController = navController,
            )
        }
    ){ innerPadding ->
        ListBody(
            exerciseList = exercises,
            contentPadding = innerPadding,
            onSelected = navigateToLogEntry,
            isEdit = isEdit,
            onRename = {exercise -> exerciseListViewModel.renameExercise(exercise)},
            onDelete = { exercise -> exerciseListViewModel.deleteExercise(exercise) },
            closeEditMode = { isEdit = false }
        )
    }
}

@Composable
fun ListBody(
    modifier: Modifier = Modifier,
    exerciseList: List<Exercise>,
    isEdit: Boolean = false,
    onRename: (Exercise) -> Unit = {},
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
                        isEdit = isEdit,
                        onRename = { exerciseR -> onRename(exerciseR)},
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

@Composable
fun ExerciseCard(
    exercise: Exercise,
    modifier: Modifier = Modifier,
    onRename: (Exercise) -> Unit = {},
    onDelete: (Exercise) -> Unit = {},
    closeEditMode: () -> Unit = {},
    isEdit: Boolean = false
) {
    var showRenameDialog by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
    ) {
        Row (
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (isEdit and exercise.isCustom){
                Icon(
                    modifier = Modifier.clickable { onDelete(exercise)},
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Clear"
                )
                Text(
                    text = exercise.name,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { showRenameDialog = true }) {
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
        if (showRenameDialog) {
            WorkoutModal(
                currentName = exercise.name,
                onConfirm = { newName ->
                    onRename(exercise.copy(name = newName))
                    showRenameDialog = false
                    closeEditMode()
                },
                onDismiss = {
                    showRenameDialog = false
                    closeEditMode()
                }
            )
        }
    }
}

@Composable
fun EmptyList(
    modifier: Modifier = Modifier,
    text: String
){
    Card(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
    ) {
        Text(
            text = text,
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
        EmptyList(text = stringResource(R.string.no_exercises_card))
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