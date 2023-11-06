package com.dvinov.traynote.navigation

import com.dvinov.traynote.db.Note

sealed class NavigationEvent {
    data class NavigateToNote(val note: Note?) : NavigationEvent()
    object NavigateBack : NavigationEvent()
}