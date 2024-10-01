package com.example.exerciseapp

import android.app.Application
import com.example.exerciseapp.data.AppContainer
import com.example.exerciseapp.data.AppDataContainer

class ExerciseApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}