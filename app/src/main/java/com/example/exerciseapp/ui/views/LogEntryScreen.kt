
    @file:OptIn(ExperimentalMaterial3Api::class)
    package com.example.exerciseapp.ui.views

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.gestures.detectTapGestures
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.lazy.itemsIndexed
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.input.nestedscroll.nestedScroll
    import androidx.compose.ui.input.pointer.pointerInput
    import androidx.compose.ui.platform.LocalFocusManager
    import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    import com.example.exerciseapp.data.model.ExerciseSet
    import com.example.exerciseapp.data.model.LogWithSets
    import com.example.exerciseapp.ui.AppViewModelProvider
    import com.example.exerciseapp.ui.navigation.NavigationDestination
    import com.example.exerciseapp.ui.viewmodels.LogEntryUiState
    import com.example.exerciseapp.ui.viewmodels.LogViewModel

    object LogEntryDestination : NavigationDestination {
        override val route = "log_entry_screen"
        override val titleRes = R.string.log_entry_screen
        const val exerciseIdArg = "exerciseId"
        val routeWithArgs = "$route/{$exerciseIdArg}"
    }

    @Composable
    fun LogEntryScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
        onNavigateUp: () -> Unit,
        logViewModel: LogViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val uiState by logViewModel.uiState.collectAsState()

        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = stringResource(R.string.log_entry_screen),
                    scrollBehavior = scrollBehavior,
                    leftIcon = {
                        TopIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            onClick = onNavigateUp
                        )
                    },
                )
            },
            bottomBar = {
                BottomNavigation(
                    navController = navController
                )
            }
        ) { innerPadding ->
            LogEntryBody(
                uiState = uiState,
                onWeightChange = logViewModel::onWeightChange,
                onRepsChange = logViewModel::onRepsChange,
                onNotesChange = logViewModel::onNotesChange,
                onSubmitSet = logViewModel::submitSet,
                onSubmitLog = logViewModel::submitLog,
                onEdit = logViewModel::editSet,
                onDeleteSet = logViewModel::deleteSet,
                contentPadding = innerPadding
            )
        }
    }

    @Composable
    fun LogEntryBody(
        uiState: LogEntryUiState,
        onWeightChange: (String) -> Unit,
        onRepsChange: (String) -> Unit,
        onNotesChange: (String) -> Unit,
        onSubmitSet: () -> Unit,
        onSubmitLog: () -> Unit,
        onEdit: (Int) -> Unit,
        onDeleteSet: (Int) -> Unit,
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(0.dp)
    ) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                }
                .padding(contentPadding)
        ) {
            ExerciseHeader(exerciseName = uiState.exerciseName)
            InputFields(
                weightInput = uiState.weightInput,
                repsInput = uiState.repsInput,
                notesInput = uiState.logDetails.notes,
                onWeightChange = onWeightChange,
                onRepsChange = onRepsChange,
                onNotesChange = onNotesChange
            )
            ActionButtons(
                isEditing = uiState.isEditing,
                onSubmitSet = onSubmitSet,
                onSubmitLog = onSubmitLog,
                uiState = uiState
            )
            WorkoutSets(
                uiState = uiState,
                onEdit = onEdit,
                onDeleteSet = onDeleteSet
            )
        }
    }

    @Composable
    fun ExerciseHeader(exerciseName: String) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small))
        )
    }

    @Composable
    fun InputFields(
        weightInput: String,
        repsInput: String,
        notesInput: String,
        onWeightChange: (String) -> Unit,
        onRepsChange: (String) -> Unit,
        onNotesChange: (String) -> Unit
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = onWeightChange,
                    label = { Text("Weight") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = repsInput,
                    onValueChange = onRepsChange,
                    label = { Text("Reps") },
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                minLines = 3,
                value = notesInput,
                onValueChange = onNotesChange,
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }

    @Composable
    fun ActionButtons(
        isEditing: Boolean,
        onSubmitSet: () -> Unit,
        onSubmitLog: () -> Unit,
        uiState: LogEntryUiState
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            horizontalArrangement = Arrangement.Center
        ) {
            if (isEditing) {
                Button(onClick = onSubmitSet) {
                    Text(text = "Update")
                }
            } else {
                Button(onClick = onSubmitSet) {
                    Text(text = "Add")
                }
            }
            if (uiState.sets.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSubmitLog,
                ) {
                    Text(text = "Submit")
                }
            }
        }
    }

    @Composable
    fun WorkoutSets(
        uiState: LogEntryUiState,
        onEdit: (Int) -> Unit,
        onDeleteSet: (Int) -> Unit,
        ){
        Row{

                CurrentWorkoutSets(
                    sets = uiState.sets,
                    onEdit = onEdit,
                    onDeleteSet = onDeleteSet
                )
                PreviousWorkoutSets(previousLog = uiState.previousLog)

        }
    }

    @Composable
    fun CurrentWorkoutSets(
        sets: List<ExerciseSet>,
        onEdit: (Int) -> Unit,
        onDeleteSet: (Int) -> Unit
    ) {
        Column(    modifier = Modifier
            .fillMaxWidth(.6f)
        ) {
            Row(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Current Workout:", style = MaterialTheme.typography.bodyLarge)
            }
            if (sets.isEmpty()) {
                Row(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "No sets added.")
                }
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
        }

    }

    @Composable
    fun PreviousWorkoutSets(
        previousLog: LogWithSets?
    ) {
        Column(    modifier = Modifier
            .fillMaxWidth(1f)
        ) {
            Row(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Last Workout:", style = MaterialTheme.typography.bodyLarge)
            }
            if(previousLog  != null){
                    LazyColumn {
                        items(previousLog.sets) { set ->
                            SetRowReadOnly(set = set)
                        }
                    }
            }else {
                Row(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "No previous sets.")
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
                .padding(dimensionResource(id = R.dimen.padding_small))
                .clickable { onEdit(index) },
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
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${set.weight} kg x ${set.reps} reps")
        }
    }