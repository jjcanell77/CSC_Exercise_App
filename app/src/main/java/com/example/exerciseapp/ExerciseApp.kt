package com.example.exerciseapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.exerciseapp.data.model.BottomNavigationItem
import com.example.exerciseapp.ui.navigation.AppNavHost
import com.example.exerciseapp.ui.views.HomeDestination
import com.example.exerciseapp.ui.views.ProgramDestination
import com.example.exerciseapp.ui.views.WorkoutDestination

@Composable
fun ExerciseApp(
    navController: NavHostController = rememberNavController()
) {
    AppNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit = {},
    isSearching: Boolean,
    onToggleSearch: (Boolean) -> Unit = {},
    close: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchText,
                onQueryChange = onSearchTextChange,
                onSearch = {
                    onSearchTextChange(it)
                    keyboardController?.hide()
                },
                expanded = false,
                onExpandedChange = onToggleSearch,
                enabled = true,
                placeholder = {
                    Row {
                        Icon(imageVector = Filled.Search, contentDescription = "Search")
                        Text(text = "Exercises")
                    }
                },
                trailingIcon = {
                    if (isSearching) {
                        IconButton(
                            onClick = {
                                close()
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(
                                imageVector = Filled.Clear,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                },
                interactionSource = null
            )
        },
        expanded = false,
        onExpandedChange = onToggleSearch,
        modifier = modifier,
        content = {
        }
    )
}


@Composable
fun BottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavigationItem(
            title = "exercises",
            selectedIcon = R.drawable.exercises_active,
            unSelectedIcon = R.drawable.exercises_inactive,
            navigate = {
                navController.navigate(HomeDestination.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            route = HomeDestination.route
        ),
        BottomNavigationItem(
            title = "workouts",
            selectedIcon = R.drawable.workouts_active,
            unSelectedIcon = R.drawable.workouts_inactive,
            navigate = {
                navController.navigate(WorkoutDestination.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            route = WorkoutDestination.route
        ),
        BottomNavigationItem(
            title = "programs",
            selectedIcon = R.drawable.programs_active,
            unSelectedIcon = R.drawable.programs_inactive,
            navigate = {
                navController.navigate(ProgramDestination.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            route = ProgramDestination.route
        )
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/")

    NavigationBar(
        containerColor = Color(0xFFF7F7F7)
    ) {
        val primaryColor = Color(0xFFF7F7F7)
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        item.navigate()
                    }
                },
                enabled = !isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primaryColor,
                    selectedTextColor = primaryColor,
                    indicatorColor = primaryColor,
                    unselectedIconColor = primaryColor,
                    unselectedTextColor = primaryColor,
                    disabledIconColor = primaryColor,
                    disabledTextColor = primaryColor
                ),
                icon = {
                    Image(
                        painter = painterResource(
                            id = if (isSelected) item.selectedIcon else item.unSelectedIcon
                        ),
                        contentDescription = item.title,
                        modifier = Modifier.aspectRatio(3F, false),
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    leftIcon: @Composable () -> Unit = {},
    rightIcon: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            leftIcon()
        },
        actions = {
            rightIcon()
        }
    )
}

@Composable
fun TopIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}