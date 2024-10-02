package com.example.exerciseapp.data.database

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 1. Rename the existing table
        database.execSQL("ALTER TABLE muscle_group RENAME TO muscle_group_old")

        // 2. Create the new table with the desired schema
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS muscle_group (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                imageName TEXT NOT NULL DEFAULT ''
            )
        """.trimIndent())

        // 3. Copy the data from the old table to the new table
        database.execSQL("""
            INSERT INTO muscle_group (id, name, imageName)
            SELECT id, name, '' FROM muscle_group_old
        """.trimIndent())

        // 4. Drop the old table
        database.execSQL("DROP TABLE muscle_group_old")

        try {
            val muscleGroups = listOf(
                "Chest" to "chest",
                "Back" to "back",
                "Shoulders" to "shoulders",
                "Arms" to "arms",
                "Legs" to "legs",
                "Abs" to "abs"
            )
            muscleGroups.forEach { (name, imageName) ->
                database.execSQL(
                    "INSERT INTO muscle_group (name, imageName) VALUES (?, ?)",
                    arrayOf(name, imageName)
                )
            }
            Log.d("MIGRATION_1_2", "Data insertion complete during migration")
        } catch (e: Exception) {
            Log.e("MIGRATION_1_2", "Error inserting data during migration", e)
        }
    }
}
