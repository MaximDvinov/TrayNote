package com.dvinov.traynote.db

import app.cash.sqldelight.db.SqlDriver
import com.dvinov.traynote.db.Note

expect fun createDriver(): SqlDriver


fun createDatabase(): Database {
    val driver = createDriver()
    val database = Database(
        driver, noteAdapter = Note.Adapter(
            updatedAtAdapter = noteDateAdapter
        )
    )

    return database
}