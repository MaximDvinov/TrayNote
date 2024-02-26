package com.dvinov.traynote.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.navigation.NavigationEvent
import com.dvinov.traynote.repositories.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val list: List<Note> = emptyList(),
    val query: String? = null,
    val searchedList: List<Note>? = null,
)

sealed class HomeEvent {
    data class OnNoteClick(val note: Note) : HomeEvent()
    data object CreateNewNote : HomeEvent()
    data class OnSearchChange(val query: String?) : HomeEvent()
}

class HomeScreenModel(private val noteRepository: NoteRepository) : ScreenModel {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        screenModelScope.launch {
            noteRepository.getAllNotes().collect { list ->
                _state.update { oldState ->
                    oldState.copy(
                        list = list,
                        searchedList = list.filter {
                            it.content.contains(
                                oldState.query ?: "",
                                ignoreCase = true
                            ) || it.title.contains(oldState.query ?: "", ignoreCase = true)
                        })
                }
            }
        }

    }

    private val navigationEvent = Channel<NavigationEvent>()
    val navigationEventFlow = navigationEvent.receiveAsFlow()

    fun onEvent(event: HomeEvent) {
        screenModelScope.launch {
            when (event) {
                is HomeEvent.OnNoteClick -> {
                    navigationEvent.send(NavigationEvent.NavigateToNote(event.note))
                }

                HomeEvent.CreateNewNote -> {
                    navigationEvent.send(NavigationEvent.NavigateToNote(null))
                }

                is HomeEvent.OnSearchChange -> {
                    _state.update { oldState ->
                        oldState.copy(
                            query = event.query,
                            searchedList = oldState.list.filter {
                                it.content.contains(
                                    event.query ?: "",
                                    ignoreCase = true
                                ) || it.title.contains(event.query ?: "", ignoreCase = true)
                            })
                    }
                }
            }
        }
    }
}