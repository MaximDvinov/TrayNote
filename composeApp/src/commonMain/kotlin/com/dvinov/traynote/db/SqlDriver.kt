package com.dvinov.traynote.db

import app.cash.sqldelight.db.SqlDriver


expect fun createDriver(): SqlDriver


fun createDatabase(): Database {
    val driver = createDriver()

    return Database(
        driver, noteAdapter = Note.Adapter(
            updatedAtAdapter = noteDateAdapter
        )
    )
}