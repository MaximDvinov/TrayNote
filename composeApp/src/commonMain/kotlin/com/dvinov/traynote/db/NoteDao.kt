package com.dvinov.traynote.db

import kotlinx.coroutines.flow.Flow

interface NoteDao {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)
}