package com.dvinov.traynote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.screens.home.HomeScreen
import com.dvinov.traynote.screens.note.NoteScreen
import com.dvinov.traynote.theme.AppTheme

@Composable
internal fun App(isCreate: Boolean, openNote: Note?) = AppTheme {
    AppContent(isCreate, openNote)
}

@Composable
fun AppContent(isCreate: Boolean, openNote: Note?) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (isCreate || openNote != null) {
            Navigator(listOf(HomeScreen(), NoteScreen(openNote)))
        } else {
            Navigator(HomeScreen())
        }
    }
}
