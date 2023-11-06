package com.dvinov.traynote.db

import kotlinx.coroutines.flow.Flow

interface NoteDao {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Long)
    suspend fun updateNote(note: Note)
}