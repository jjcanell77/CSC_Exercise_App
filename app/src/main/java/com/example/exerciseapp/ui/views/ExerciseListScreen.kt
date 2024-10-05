@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.exerciseapp.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
    val muscleGroupList by exerciseListViewModel.muscleGroupList.collectAsState()
    var isEdit by remember { mutableStateOf(false) }
//    var showEntryModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = title,
                scrollBehavior = scrollBehavior,
                leftIcon ={ TopIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", onClick = {onNavigateUp()}) },
                rightIcon = {
                    if (exercises.isNotEmpty() && exercises.any { exercise -> exercise.isCustom }) {
                        TopIcon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", onClick = {isEdit = !isEdit})
                    }
                }
            )
        },
        bottomBar = {
//            Column { {TODO} will navigate to WorkoutEntryScreen to add exercises to Workout.
//                Button(onClick = { showEntryModal = true }) {
//                    Text(text = "Add")
//                }
                BottomNavigation(
                    navController = navController,
                )
//            }
        }
    ){ innerPadding ->
        ListBody(
            exerciseList = exercises,
            contentPadding = innerPadding,
            onSelected = navigateToLogEntry,
            muscleGroupList = muscleGroupList,
            isEdit = isEdit,
            onUpdate = { newName, muscleGroupId ->
                exerciseListViewModel.updateExercise(newName, muscleGroupId)},
            onDelete = { exercise ->
                exerciseListViewModel.deleteExercise(exercise) },
            closeEditMode = { isEdit = false }
        )
    }
}

@Composable
fun ListBody(
    modifier: Modifier = Modifier,
    muscleGroupList: List<MuscleGroup>,
    exerciseList: List<Exercise>,
    isEdit: Boolean = false,
    onUpdate: (String, Int) -> Unit,
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
                        onUpdate = { newName, muscleGroupId -> onUpdate(newName, muscleGroupId)},
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
    muscleGroupList: List<MuscleGroup>,
    modifier: Modifier = Modifier,
    onUpdate: (String, Int) -> Unit,
    onDelete: (Exercise) -> Unit = {},
    closeEditMode: () -> Unit = {},
    isEdit: Boolean
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
            ExerciseModal(
                title = "Edit Exercise",
                currentName = exercise.name,
                muscleGroupList = muscleGroupList,
                onConfirm = {  newName, muscleGroupId ->
                    onUpdate( newName, muscleGroupId)
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

@Composable
fun ExerciseModal(
    title: String,
    currentName: String = "",
    currentType: Int = 1,
    onConfirm: (String, Int) -> Unit,
    muscleGroupList: List<MuscleGroup>,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var selectedMuscleGroupId by remember { mutableIntStateOf(currentType) }
    var expanded by remember { mutableStateOf(false) }
    val selectedMuscleGroupName = muscleGroupList.firstOrNull { it.id == selectedMuscleGroupId }?.name ?: ""
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("New Exercise Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedMuscleGroupName,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Muscle Group") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth()
                            .menuAnchor()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        muscleGroupList.forEach { muscleGroup ->
                            DropdownMenuItem(
                                text = { Text(muscleGroup.name) },
                                onClick = {
                                    selectedMuscleGroupId = muscleGroup.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(name, selectedMuscleGroupId)
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
