package com.dvinov.traynote.db

import app.cash.sqldelight.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.dvinov.traynote.db.Note

class NoteDaoImpl(private val database: Database) : NoteDao {
    override fun getAllNotes(): Flow<List<Note>> {
        return database.noteQueries.selectAll().asFlow().map {
            it.executeAsList()
        }
    }

    override suspend fun insertNote(note: Note) {
        database.noteQueries.insert(note)
    }

    override suspend fun deleteNote(note: Long) {
        database.noteQueries.deleteById(note)
    }

    override suspend fun updateNote(note: Note) {
        database.noteQueries.updateNoteById(
            id = note.id,
            title = note.title,
            content = note.content,
            updatedAt = note.updatedAt,
            isPinned = note.isPinned
        )
    }
}