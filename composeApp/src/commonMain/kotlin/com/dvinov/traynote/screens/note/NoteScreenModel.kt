package com.dvinov.traynote.screens.note

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.navigation.NavigationEvent
import com.dvinov.traynote.repositories.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class NoteState(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
)

sealed class NoteScreenEvent {
    object OnNoteSave : NoteScreenEvent()
    object OnNoteDelete : NoteScreenEvent()
    data class OnNoteTitleChange(val title: String) : NoteScreenEvent()
    data class OnNoteContentChange(val content: String) : NoteScreenEvent()
}

class NoteScreenModel(private val noteRepository: NoteRepository) : ScreenModel {
    private val _noteState: MutableStateFlow<NoteState> = MutableStateFlow(NoteState())
    val noteState = _noteState.asStateFlow()

    private val navigationEvent = Channel<NavigationEvent>()
    val navigationEventFlow = navigationEvent.receiveAsFlow()

    fun noteInit(note: Note) {
        _noteState.update { it.copy(id = note.id, title = note.title, content = note.content) }
    }

    fun onEvent(event: NoteScreenEvent) {
        screenModelScope.launch {
            when (event) {
                is NoteScreenEvent.OnNoteSave -> {
                    if (_noteState.value.content.isBlank()) return@launch

                    navigationEvent.send(NavigationEvent.NavigateBack)
                    val clock = Clock.System.now()
                    if (_noteState.value.id != null) {
                        noteRepository.updateNote(
                            Note(
                                id = _noteState.value.id!!,
                                title = _noteState.value.title,
                                content = _noteState.value.content,
                                updatedAt = clock.toLocalDateTime(TimeZone.UTC)
                            )
                        )
                    } else {
                        noteRepository.insertNote(
                            Note(
                                id = 0,
                                title = _noteState.value.title,
                                content = _noteState.value.content,
                                updatedAt = clock.toLocalDateTime(TimeZone.UTC),
                            )
                        )
                    }
                }

                is NoteScreenEvent.OnNoteDelete -> {
                    _noteState.value.id?.let {
                        noteRepository.deleteNote(it)
                    }

                    navigationEvent.send(NavigationEvent.NavigateBack)
                }

                is NoteScreenEvent.OnNoteTitleChange -> {
                    _noteState.update {
                        it.copy(title = event.title)
                    }
                }

                is NoteScreenEvent.OnNoteContentChange -> {
                    _noteState.update {
                        it.copy(content = event.content)
                    }
                }
            }
        }
    }
}