
    @file:OptIn(ExperimentalMaterial3Api::class)
    package com.example.exerciseapp.ui.views

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.lazy.itemsIndexed
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.outlined.Clear
    import androidx.compose.material3.Button
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBarDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.input.nestedscroll.nestedScroll
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.example.exerciseapp.BottomNavigation
    import com.example.exerciseapp.R
    import com.example.exerciseapp.TopAppBar
    import com.example.exerciseapp.data.model.Exercise
    import com.example.exerciseapp.data.model.ExerciseSet
    import com.example.exerciseapp.data.model.Log
    import com.example.exerciseapp.data.model.LogWithSets
    import com.example.exerciseapp.ui.AppViewModelProvider
    import com.example.exerciseapp.ui.navigation.NavigationDestination
    import com.example.exerciseapp.ui.theme.ExerciseAppTheme
    import com.example.exerciseapp.ui.viewmodels.LogViewModel

    object LogEntryDestination : NavigationDestination {
        override val route = "log_entry_screen"
        override val titleRes = R.string.log_entry_screen
        const val exerciseIdArg = "exerciseId"
        val routeWithArgs = "$route/{$exerciseIdArg}"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LogEntryScreen(
        modifier: Modifier = Modifier,
        navigateToExercise: () -> Unit,
        navigateToWorkout: () -> Unit,
        navigateToProgram: () -> Unit,
        onNavigateUp: () -> Unit,
        exerciseId: Int = 0,
        logViewModel: LogViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val exercise by logViewModel.exercise.collectAsState()
        val previousLog by logViewModel.previousLog.collectAsState()
        val logDetails by logViewModel.logDetails.collectAsState()
        val isEditing by logViewModel.isEditing.collectAsState()
        val sets by logViewModel.sets.collectAsState()

        LaunchedEffect(exerciseId) {
            logViewModel.loadExercise(exerciseId)
            logViewModel.loadPreviousLog(exerciseId)
        }

        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = exercise?.name ?: stringResource(R.string.log_entry_screen),
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
        ) { innerPadding ->
            LogEntryBody(
                logDetails = logDetails,
                exercise = exercise,
                previousLog = previousLog,
                sets = sets,
                isEditing = isEditing,
                onSetDetailsChange = logViewModel::onSetDetailsChange,
                onNotesChange = logViewModel::onNotesChange,
                onSubmitSet = logViewModel::submitSet,
                onSubmitLog = logViewModel::submitLog,
                onEdit = logViewModel::editSet,
                onDeleteSet = logViewModel::deleteSet,
                onCancelEdit = logViewModel::cancelEdit,
                contentPadding = innerPadding
            )
        }
    }

    @Composable
    fun LogEntryBody(
        logDetails: Log,
        exercise: Exercise?,
        previousLog: LogWithSets?,
        sets: List<ExerciseSet>,
        isEditing: Boolean,
        onSetDetailsChange: (String, String) -> Unit,
        onNotesChange: (String) -> Unit,
        onSubmitSet: () -> Unit,
        onSubmitLog: () -> Unit,
        onEdit: (Int) -> Unit,
        onDeleteSet: (Int) -> Unit,
        onCancelEdit: () -> Unit,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(0.dp)
    ) {
        var weightInput by remember { mutableStateOf("") }
        var repsInput by remember { mutableStateOf("") }
        val currentSetIndex = if (isEditing) sets.lastIndex else -1

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(contentPadding)
        ) {
            exercise?.let {
                Text(text = it.name, style = MaterialTheme.typography.headlineLarge)
            }

            // Input Fields for Weight and Reps
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = {
                        weightInput = it
                        onSetDetailsChange(weightInput, repsInput)
                    },
                    label = { Text("Weight") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = repsInput,
                    onValueChange = {
                        repsInput = it
                        onSetDetailsChange(weightInput, repsInput)
                    },
                    label = { Text("Reps") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Notes Input
            OutlinedTextField(
                value = logDetails.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Submit or Edit Button
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (isEditing) {
                    Button(onClick = onCancelEdit) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onSubmitSet) {
                        Text(text = "Update Set")
                    }
                } else {
                    Button(onClick = onSubmitSet) {
                        Text(text = "Add Set")
                    }
                }
            }

            // Current Workout Sets
            Text(text = "Current Workout:", style = MaterialTheme.typography.bodyLarge)
            if (sets.isEmpty()) {
                Text(text = "No sets added.")
            } else {
                LazyColumn {
                    itemsIndexed(sets) { index, set ->
                        SetRow(
                            set = set,
                            index = index,
                            onEdit = onEdit,
                            onDelete = onDeleteSet
                        )
                    }
                }
            }

            // Submit Log Button
            Button(
                onClick = onSubmitLog,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Submit Log")
            }

            // Previous Workout Sets
            previousLog?.let { log ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Last Workout:", style = MaterialTheme.typography.bodyLarge)
                if (log.sets.isEmpty()) {
                    Text(text = "No previous sets.")
                } else {
                    LazyColumn {
                        items(log.sets) { set ->
                            SetRowReadOnly(set = set)
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun SetRow(
        set: ExerciseSet,
        index: Int,
        onEdit: (Int) -> Unit,
        onDelete: (Int) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEdit(index) }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Set ${index + 1}: ${set.weight} kg x ${set.reps} reps")
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onDelete(index) }) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Delete"
                )
            }
        }
    }

    @Composable
    fun SetRowReadOnly(set: ExerciseSet) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${set.weight} kg x ${set.reps} reps")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun LogEntryScreenPreview () {
        ExerciseAppTheme {
            LogEntryScreen(
                navigateToExercise = {  },
                navigateToProgram = {  },
                navigateToWorkout = {  },
                onNavigateUp = {  }
            )
        }
    }