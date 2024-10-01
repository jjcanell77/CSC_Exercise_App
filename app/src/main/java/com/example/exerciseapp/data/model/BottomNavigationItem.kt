package com.example.exerciseapp.data.model

data class  BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int,
    val navigate: () -> Unit
)