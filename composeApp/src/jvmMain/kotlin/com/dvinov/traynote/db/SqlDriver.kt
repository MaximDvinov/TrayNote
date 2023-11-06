package com.dvinov.traynote.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ca.gosyer.appdirs.AppDirs
import com.dvinov.traynote.db.Database
import java.io.File


actual fun createDriver(): SqlDriver {
    val path = File(System.getProperty("java.io.tmpdir"), "note.db")
    val driver: SqlDriver =
        JdbcSqliteDriver(url = "jdbc:sqlite:${path}").also {
            Database.Schema.create(it)
        }

    return driver
}
