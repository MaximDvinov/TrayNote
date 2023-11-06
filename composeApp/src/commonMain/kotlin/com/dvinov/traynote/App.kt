package com.dvinov.traynote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dvinov.traynote.screens.home.HomeScreen
import com.dvinov.traynote.screens.note.NoteScreen
import com.dvinov.traynote.theme.AppTheme

@Composable
internal fun App(isCreate: Boolean) = AppTheme {
    AppContent(isCreate)
}

@Composable
fun AppContent(isCreate: Boolean) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (isCreate) {
            Navigator(listOf(HomeScreen(), NoteScreen(null)))
        } else {
            Navigator(HomeScreen())
        }


    }
}
