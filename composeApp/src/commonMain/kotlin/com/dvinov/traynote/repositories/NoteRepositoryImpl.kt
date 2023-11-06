package com.dvinov.traynote.repositories

import com.dvinov.traynote.db.Note
import com.dvinov.traynote.db.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = dao.getAllNotes()

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Long) {
        dao.deleteNote(note)
    }

    override suspend fun updateNote(note: Note) {
        dao.updateNote(note)
    }
}