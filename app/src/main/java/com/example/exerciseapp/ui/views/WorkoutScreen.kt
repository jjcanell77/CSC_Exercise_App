@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.exerciseapp.BottomNavigation
import com.example.exerciseapp.TopIcon
import com.example.exerciseapp.R
import com.example.exerciseapp.TopAppBar
import com.example.exerciseapp.data.model.Workout
import com.example.exerciseapp.ui.AppViewModelProvider
import com.example.exerciseapp.ui.navigation.NavigationDestination
import com.example.exerciseapp.ui.theme.ExerciseAppTheme
import com.example.exerciseapp.ui.viewmodels.WorkoutViewModel

object WorkoutDestination : NavigationDestination {
    override val route = "workout_screen"
    override val titleRes = R.string.workout_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen (
    navController: NavController,
    navigateToWorkoutEntry: (String) -> Unit,
    navigateToExerciseList: (Int) -> Unit,
    modifier: Modifier = Modifier,
    workoutViewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val workoutList by workoutViewModel.workouts.collectAsState()
    var isEdit by remember { mutableStateOf(false) }
    var showWorkoutModal by remember { mutableStateOf(false) }

    Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
        TopAppBar(
            title = stringResource(R.string.workout_screen_title),
            rightIcon ={
                if(workoutList.isNotEmpty()){
                    TopIcon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", onClick = {isEdit = !isEdit})
                }
            },
            leftIcon = {TopIcon(imageVector = Icons.Filled.Add, contentDescription = "Add", onClick = {showWorkoutModal = true})},
            scrollBehavior = scrollBehavior
        )
    },
    bottomBar = {
        BottomNavigation(
            navController = navController
        )
    }
    ){ innerPadding ->
        WorkoutBody(
            workoutList = workoutList,
            isEdit = isEdit,
            contentPadding = innerPadding,
            onSelected = navigateToExerciseList,
            closeEditMode = { isEdit = false },
            workoutViewModel = workoutViewModel
        )
        if (showWorkoutModal) {
            WorkoutModal(
                currentName = "",
                onConfirm = { newName ->
                    showWorkoutModal = false
                    navigateToWorkoutEntry(newName)
                },
                onDismiss = {
                    showWorkoutModal = false
                }
            )
        }
    }
}

@Composable
fun WorkoutBody(
    modifier: Modifier = Modifier,
    workoutList: List<Workout>,
    isEdit: Boolean = false,
    onSelected: (Int) -> Unit = {},
    closeEditMode: () -> Unit = {},
    workoutViewModel: WorkoutViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(contentPadding)
    ) {
        if(workoutList.isEmpty()){
            EmptyList(text = stringResource(R.string.no_workouts_card))
        }
        else{
            LazyColumn{
                items(workoutList) { workout ->
                    WorkoutCard(
                        workout = workout,
                        isEdit = isEdit,
                        workoutViewModel = workoutViewModel,
                        closeEditMode = closeEditMode,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_xsmall))
                            .clickable(enabled = !isEdit) { onSelected(workout.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: Workout,
    modifier: Modifier = Modifier,
    workoutViewModel: WorkoutViewModel,
    isEdit: Boolean,
    closeEditMode: () -> Unit = {}
    ) {
    var showRenameDialog by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
    ) {
        Row (
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (isEdit and workout.isCustom){
                Icon(
                    modifier = Modifier.clickable { workoutViewModel.deleteWorkout(workout)},
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Clear"
                )
                Text(
                    text = workout.name,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { showRenameDialog = true }) {
                    Text(text = "rename workout")
                }
            }
            else {
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
        if (showRenameDialog) {
            WorkoutModal(
                currentName = workout.name,
                onConfirm = { newName ->
                    workoutViewModel.renameWorkout(workout.copy(name = newName))
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
fun WorkoutModal(
    currentName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Rename Workout") },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("New Workout Name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(newName)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RenameWorkoutDialogPreview () {
    ExerciseAppTheme {
        WorkoutModal("Chest Day", {}, {})
    }
}