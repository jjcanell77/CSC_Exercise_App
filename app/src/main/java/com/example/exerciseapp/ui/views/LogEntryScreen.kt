
    @file:OptIn(ExperimentalMaterial3Api::class)
    package com.example.exerciseapp.ui.views

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Button
    import androidx.compose.material3.Card
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TopAppBarDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.input.nestedscroll.nestedScroll
    import androidx.compose.ui.res.dimensionResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import com.example.exerciseapp.BottomNavigation
    import com.example.exerciseapp.R
    import com.example.exerciseapp.TopAppBar
    import com.example.exerciseapp.ui.navigation.NavigationDestination
    import com.example.exerciseapp.ui.theme.ExerciseAppTheme

    object LogEntryDestination : NavigationDestination {
        override val route = "log_entry_screen"
        override val titleRes = R.string.log_entry_screen
        const val exerciseIdArg = "exerciseId"
        val routeWithArgs = "$route/{$exerciseIdArg}"
    }

    @Composable
    fun LogEntryScreen (
        navigateBack: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = stringResource(R.string.log_entry_screen),
                    canNavigateBack = true,
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                BottomNavigation()
            }
        ){ innerPadding ->
            LogEntryBody(
                contentPadding = innerPadding,
            )
        }
    }

    @Composable
    fun LogEntryBody(
        logDetails: LogDetails,
        onValueChange: (LogDetails) -> Unit = {},
        modifier: Modifier = Modifier,
        contentPadding: PaddingValues = PaddingValues(0.dp)
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(contentPadding)
        ) {
            Card {
                Row {
                    Text(text = logDetails.exercise)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = logDetails.weight,
                        onValueChange = { onValueChange(logDetails.copy(log = it)) })
                    TextField(
                        value = logDetails.reps,
                        onValueChange = { onValueChange(logDetails.copy(log = it)) })
                }
                Row {
                    TextField(
                        value = logDetails.notes,
                        onValueChange = { onValueChange(logDetails.copy(log = it)) })
                }
                Row {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Submit")
                    }
                }
            }
//            Card {
//                Column {
//                    Text(text = "Last Workout:")
//                    items(logDetails) { set ->
//                       Row {
//                           Text(text = set.weight)
//                           Text(text = " - ")
//                           Text(text = set.reps)
//                       }
//                    }
//                }
//            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun LogEntryScreenPreview () {
        ExerciseAppTheme {
            LogEntryScreen()
        }
    }