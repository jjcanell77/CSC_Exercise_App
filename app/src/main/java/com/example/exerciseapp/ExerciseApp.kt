package com.example.exerciseapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.exerciseapp.data.model.BottomNavigationItem
import com.example.exerciseapp.data.model.Exercise
import com.example.exerciseapp.ui.navigation.AppNavHost

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
    onToggleSearch: () -> Unit = {},
    close: () -> Unit = {},
    exerciseList: List<Exercise>) {
    SearchBar(
//        colors = colors().copy(
//            containerColor = Color.White,
//            dividerColor = Color.Red
//        ),
        query = searchText, // text showed on SearchBar
        onQueryChange = onSearchTextChange, // update the value of searchText
        onSearch = onSearchTextChange, // the callback for search action
        active = isSearching, // whether the user is searching or not
        onActiveChange = { onToggleSearch() }, // toggles search state
        modifier = modifier,
        placeholder = {
            Row {
                Icon(imageVector = Filled.Search, contentDescription = "Search" )
                Text(text = "Search")
            }
        },
        trailingIcon = {
            if(isSearching){
                IconButton(
                    onClick = {close()}
                ) {
                    Icon(
                        imageVector = Filled.Clear,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    ){
//        ExerciseList(
//            exerciseList = exerciseList,
//            modifier = Modifier
//                .padding(dimensionResource(id = R.dimen.padding_xsmall))
//                .clickable { }
//        )
//        LazyColumn {
//            items(exerciseList) { exercise ->
//                Text(
//                    text = exercise.name,
//                    modifier = Modifier
//                        .padding(8.dp)
//                )
//            }
//        }
    }
}

@Composable
fun BottomNavigation(
    navigateToWorkout: () -> Unit = {},
    navigateToProgram: () -> Unit = {},
    navigateToExercise: () -> Unit = {}
){
    val items = listOf(
        BottomNavigationItem(
            title = "exercises",
            selectedIcon = R.drawable.exercises_active,
            unSelectedIcon = R.drawable.exercises_inactive,
            navigate = navigateToExercise
        ),
        BottomNavigationItem(
            title = "workouts",
            selectedIcon = R.drawable.workouts_active,
            unSelectedIcon = R.drawable.workouts_inactive,
            navigate = navigateToWorkout
        ),
        BottomNavigationItem(
            title = "programs",
            selectedIcon = R.drawable.programs_active,
            unSelectedIcon = R.drawable.programs_inactive,
            navigate = navigateToProgram
        )
    )
    var selectedIcon by rememberSaveable {
        mutableIntStateOf(0)
    }
    NavigationBar(
        containerColor = Color(0xFFF7F7F7)
    ) {
        val primaryColor = Color(0xFFF7F7F7)
        items.forEachIndexed { index, item ->
            NavigationBarItem (
                selected = selectedIcon == index,
                onClick = {
                    selectedIcon = index
                    if(index != selectedIcon){
                        item.navigate
                    }
                },
//                modifier = Modifier.padding(0.dp),
                colors = NavigationBarItemColors(
                    selectedIconColor = primaryColor,
                    selectedTextColor = primaryColor,
                    selectedIndicatorColor = primaryColor,
                    unselectedIconColor = primaryColor,
                    unselectedTextColor = primaryColor,
                    disabledIconColor = primaryColor,
                    disabledTextColor = primaryColor
                ),
                icon = {
                    Image(
                        painter = if(index == selectedIcon){
                            painterResource(id = item.selectedIcon)
                        }else{
                            painterResource(id = item.unSelectedIcon)
                        },
                        contentDescription = item.title,
                                modifier = Modifier.aspectRatio(3F,false),
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
fun TopIcon (
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
){
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}